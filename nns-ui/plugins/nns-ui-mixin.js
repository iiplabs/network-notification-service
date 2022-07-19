export default ({ app }, inject) => {
    inject('newWebId', () => {
        return (
            Math.floor(Math.random() * 100) +
            2 +
            '' +
            new Date().getTime() +
            Math.floor(Math.random() * 100) +
            2
        )
    })

    inject('sleep', (ms) => {
        return new Promise(resolve => setTimeout(resolve, ms));
    })
}
