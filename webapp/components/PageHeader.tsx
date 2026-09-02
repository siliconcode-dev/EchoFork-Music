export default function PageHeader({
  eyebrow,
  title,
  subtitle,
}: {
  eyebrow: string;
  title: string;
  subtitle?: string;
}) {
  return (
    <div className="mx-auto max-w-3xl px-6 pb-4 pt-16 text-center sm:pt-24">
      <span className="text-xs font-medium uppercase tracking-wide text-accent">
        {eyebrow}
      </span>
      <h1 className="mt-3 font-display text-3xl font-medium text-ink sm:text-4xl">
        {title}
      </h1>
      {subtitle && (
        <p className="mx-auto mt-4 max-w-xl text-sm leading-relaxed text-body">
          {subtitle}
        </p>
      )}
    </div>
  );
}
