import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import './App.css'

const container = document.getElementById('root')
const eventId = container.dataset.eventId

createRoot(container).render(<App eventId={eventId} />)
