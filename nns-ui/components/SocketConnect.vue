<template>
    <div />
</template>

<script>
export default {
    name: 'SocketConnect',

    data: () => ({
        socketInfo: {},

        keepAliveId: 0
    }),

    created() {
        const userId = this.$newWebId()
        const status = 'DISCONNECTED'

        this.socketInfo = {
            userId,
            status
        }

        // broadcast the event
        this.$root.$emit('onSocketInfoUpdated', this.socketInfo)

        this.socketConnect()
    },

    methods: {
        socketConnect() {
            const url = `${this.$config.socketUrl}`
            console.log(`Connecting Web Socket to ${url}`)

            const socket = new WebSocket(url);

            socket.onopen = () => {
                console.log("Web Socket connected");

                this.socketInfo = { ...this.socketInfo, status: 'CONNECTED' }
                this.$root.$emit('onSocketInfoUpdated', this.socketInfo)

                this.keepAliveId = setInterval(() => {
                    const messageType = 'keep_alive'
                    const { userId } = this.socketInfo
                    const timeStamp = new Date().toISOString()
                    const keepAlive = {
                        messageType,
                        userId,
                        timeStamp,
                    };
                    if (socket.readyState === socket.OPEN) {
                        socket.send(JSON.stringify(keepAlive))
                    }
                }, 540000);
            };

            socket.onerror = function () {
                console.log("Web Socket error");
            };

            socket.onclose = () => {
                console.log("Web Socket closed.");
                clearInterval(this.keepAliveId);

                this.socketInfo = { ...this.socketInfo, status: 'DISCONNECTED' }
                this.$root.$emit('onSocketInfoUpdated', this.socketInfo)

                // reconnect
                this.socketConnect();
            };

            socket.onmessage = (message) => {
                const { data } = message;
                const messageData = JSON.parse(data);
                console.log(messageData);
            }
        }
    }
}
</script>
