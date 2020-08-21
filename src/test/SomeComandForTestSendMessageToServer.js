//Spring Boot REST: тестироем rest методы

// GET all
fetch('/product/').then(response => response.json().then(console.log))

// GET one
fetch('/product/2').then(response => response.json().then(console.log))

// POST add new one
fetch(
    '/product/create',
    {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: 'Microsoft keyboard', id: 10 })
    }
).then(result => result.json().then(console.log))

// PUT save existing
fetch(
    '/product/2',
    {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text: 'Apple iMac', id: 10 })
    }
).then(result => result.json().then(console.log));

// DELETE existing
fetch('/product/2', { method: 'DELETE' }).then(result => console.log(result))

