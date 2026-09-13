async function request(url, method = 'GET') {
  const response = await fetch(url, {
    method,
    headers: { Accept: 'application/json' },
    credentials: 'same-origin'
  })
  if (!response.ok) {
    throw new Error('Errore ' + response.status)
  }
  return response.json()
}

export function getEvent(eventId) {
  return request('/api/events/' + eventId)
}

export function getPhotos(eventId) {
  return request('/api/events/' + eventId + '/photos')
}

export function getCart() {
  return request('/api/cart')
}

// nel carrello si mettono i pacchetti, cioè gli eventi
export function addToCart(eventId) {
  return request('/api/cart/items/' + eventId, 'POST')
}

export function removeFromCart(eventId) {
  return request('/api/cart/items/' + eventId, 'DELETE')
}
