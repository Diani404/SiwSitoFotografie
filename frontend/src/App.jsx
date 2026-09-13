import { useEffect, useState } from 'react'
import { getEvent, getPhotos, getCart, addToCart, removeFromCart } from './api.js'

function formatPrice(value) {
  return Number(value).toFixed(2).replace('.', ',') + ' €'
}

export default function App({ eventId }) {
  const id = Number(eventId)
  const [event, setEvent] = useState(null)
  const [photos, setPhotos] = useState([])
  const [cartIds, setCartIds] = useState([])
  const [filter, setFilter] = useState('')
  const [selected, setSelected] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    Promise.all([getEvent(id), getPhotos(id), getCart()])
      .then(([eventData, photoList, cart]) => {
        setEvent(eventData)
        setPhotos(photoList)
        setCartIds(cart.eventIds)
      })
      .catch(e => setError(e.message))
      .finally(() => setLoading(false))
  }, [id])

  // chiusura della foto ingrandita con il tasto Esc
  useEffect(() => {
    if (selected === null) return
    const onKey = e => { if (e.key === 'Escape') setSelected(null) }
    window.addEventListener('keydown', onKey)
    return () => window.removeEventListener('keydown', onKey)
  }, [selected])

  const inCart = cartIds.includes(id)

  function togglePackage() {
    const call = inCart ? removeFromCart : addToCart
    call(id)
      .then(cart => setCartIds(cart.eventIds))
      .catch(e => setError(e.message))
  }

  const text = filter.toLowerCase()
  const visible = photos.filter(p =>
    p.title.toLowerCase().includes(text) ||
    (p.description ?? '').toLowerCase().includes(text)
  )

  if (error) {
    return <p className="error">Impossibile caricare la galleria. {error}</p>
  }

  return (
    <div className="gallery">
      {event && (
        <div className="pacchetto-barra">
          <div>
            <span className="sopratitolo">Pacchetto completo</span>
            <strong>{photos.length} foto in alta risoluzione</strong>
          </div>
          <div className="pacchetto-azione">
            <span className="prezzo">{formatPrice(event.price)}</span>
            <button
              type="button"
              className={inCart ? 'bottone-secondario' : ''}
              onClick={togglePackage}>
              {inCart ? 'Togli dal carrello' : 'Aggiungi al carrello'}
            </button>
            {inCart && <a href="/cart">vai al carrello</a>}
          </div>
        </div>
      )}

      <div className="gallery-bar">
        <label>
          Cerca nella galleria
          <input
            type="text"
            value={filter}
            placeholder="titolo o descrizione"
            onChange={e => setFilter(e.target.value)}
          />
        </label>
        <span className="gallery-count">
          {loading ? 'Caricamento...' : visible.length + ' di ' + photos.length + ' foto'}
        </span>
      </div>

      {!loading && visible.length === 0 && <p>Nessuna foto corrisponde alla ricerca.</p>}

      <div className="gallery-grid">
        {visible.map(p => (
          <figure key={p.id} className="gallery-item" onClick={() => setSelected(p)}>
            <img src={p.imageUrl} alt={p.title} />
            <figcaption>
              {p.title}
              {p.cover && <span className="etichetta">copertina</span>}
            </figcaption>
          </figure>
        ))}
      </div>

      {selected && (
        <div className="lightbox" onClick={() => setSelected(null)}>
          <div className="lightbox-box" onClick={e => e.stopPropagation()}>
            <img src={selected.imageUrl} alt={selected.title} />
            <div className="lightbox-text">
              <h3>{selected.title}</h3>
              {selected.description && <p>{selected.description}</p>}
              <div className="azioni">
                <a className="bottone bottone-secondario" href={'/photos/' + selected.id}>Pagina della foto</a>
                <button type="button" className="bottone-secondario" onClick={() => setSelected(null)}>Chiudi</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
