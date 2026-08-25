import {
  useEffect,
  useState,
} from 'react'

const slides = [
  {
    eyebrow: 'SUA JORNADA NO BRASIL',
    title: 'Informação para avançar com confiança.',
    description:
      'Encontre orientações claras e baseadas em fontes oficiais.',
    highlights: [
      'Fontes oficiais',
      'Informação confiável',
    ],
  },
  {
    eyebrow: 'TUDO EM UM SÓ LUGAR',
    title: 'Seus documentos, no seu ritmo.',
    description:
      'Acompanhe o que já foi concluído e descubra o próximo passo.',
    highlights: [
      'Checklist personalizado',
      'Progresso organizado',
    ],
  },
  {
    eyebrow: 'FEITO PARA SUA REALIDADE',
    title: 'Uma jornada que começa por você.',
    description:
      'Receba orientações de acordo com seu perfil e momento migratório.',
    highlights: [
      'Perfil personalizado',
      'Apoio multilíngue',
    ],
  },
]

function AuthCarousel() {
  const [activeSlide, setActiveSlide] = useState(0)

  useEffect(() => {
    const interval = window.setInterval(() => {
      setActiveSlide(
        (currentSlide) =>
          (currentSlide + 1) % slides.length,
      )
    }, 5000)

    return () => {
      window.clearInterval(interval)
    }
  }, [])

  const slide = slides[activeSlide]

  return (
    <section
      className="auth-carousel"
      aria-label="Benefícios do CHEGA"
      aria-live="polite"
    >
      <div
        className="auth-carousel-slide"
        key={activeSlide}
      >
        <p className="eyebrow">
          {slide.eyebrow}
        </p>

        <h1>{slide.title}</h1>

        <p className="auth-carousel-description">
          {slide.description}
        </p>

        <div className="auth-carousel-highlights">
          {slide.highlights.map((highlight) => (
            <span key={highlight}>
              ✓ {highlight}
            </span>
          ))}
        </div>
      </div>

      <div
        className="auth-carousel-controls"
        aria-label="Selecionar mensagem"
      >
        {slides.map((currentSlide, index) => (
          <button
            className={
              index === activeSlide
                ? 'carousel-dot active'
                : 'carousel-dot'
            }
            key={currentSlide.title}
            type="button"
            aria-label={`Mostrar mensagem ${index + 1}`}
            aria-current={
              index === activeSlide
                ? 'true'
                : undefined
            }
            onClick={() => setActiveSlide(index)}
          />
        ))}
      </div>
    </section>
  )
}

export default AuthCarousel