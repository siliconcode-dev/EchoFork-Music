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
    <div className="border-b-2 border-ink bg-surface">
      <div className="mx-auto max-w-3xl px-6 pb-10 pt-16 text-center sm:pt-24">
        <span className="font-mono text-xs font-medium uppercase tracking-widest text-accent">
          [ {eyebrow} ]
        </span>
        <h1 className="mt-3 font-display text-3xl font-black uppercase text-ink sm:text-4xl">
          {title}
        </h1>
        {subtitle && (
          <p className="mx-auto mt-4 max-w-xl text-sm leading-relaxed text-body">
            {subtitle}
          </p>
        )}
      </div>
    </div>
  );
}
