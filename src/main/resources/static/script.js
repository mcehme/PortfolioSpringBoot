const fetcher = async (event) => {
    const form = event.target
    const loader = document.querySelector('#email-loading')
    const defaultDisplay = form.style.display
    loader.hidden=false
    form.style.display="none"

    try {
        const response = await fetch(form.action, {
            method: form.method,
            body: new FormData(form),
            });
        console.log(await response.ok);
        loader.hidden = await true
        form.style.display=await defaultDisplay
        if (await !response.ok) {
            alert("Invalid inputs. Please try again.")
        }

    } catch (e) {
        console.error(e);
    }
}

document.addEventListener('submit', (event) => {
    fetcher(event);
    event.preventDefault();
})