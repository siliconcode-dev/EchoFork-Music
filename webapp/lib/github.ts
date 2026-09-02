const REPO = "siliconcode-dev/EchoFork-Music";
const REVALIDATE_SECONDS = 3600; // 1 hour — avoids hammering the GitHub API.

export type RepoStats = {
  stars: number;
  downloads: number;
};

type GitHubRelease = {
  assets: { download_count: number }[];
};

const FALLBACK: RepoStats = { stars: 0, downloads: 0 };

export async function getRepoStats(): Promise<RepoStats> {
  try {
    const [repoRes, releasesRes] = await Promise.all([
      fetch(`https://api.github.com/repos/${REPO}`, {
        next: { revalidate: REVALIDATE_SECONDS },
        headers: { Accept: "application/vnd.github+json" },
      }),
      fetch(`https://api.github.com/repos/${REPO}/releases`, {
        next: { revalidate: REVALIDATE_SECONDS },
        headers: { Accept: "application/vnd.github+json" },
      }),
    ]);

    if (!repoRes.ok || !releasesRes.ok) {
      return FALLBACK;
    }

    const repo = (await repoRes.json()) as { stargazers_count?: number };
    const releases = (await releasesRes.json()) as GitHubRelease[];

    const downloads = Array.isArray(releases)
      ? releases.reduce(
          (sum, release) =>
            sum +
            release.assets.reduce(
              (assetSum, asset) => assetSum + asset.download_count,
              0,
            ),
          0,
        )
      : 0;

    return {
      stars: repo.stargazers_count ?? 0,
      downloads,
    };
  } catch {
    return FALLBACK;
  }
}
