export default function LegalSection({
  title,
  children,
}: {
  title: string;
  children: React.ReactNode;
}) {
  return (
    <section className="mt-10 border-t-2 border-ink pt-6">
      <h2 className="font-display text-base font-bold uppercase text-ink">
        {title}
      </h2>
      <div className="mt-3 space-y-3 text-sm leading-relaxed text-body">
        {children}
      </div>
    </section>
  );
}
