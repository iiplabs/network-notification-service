<template>
  <v-container fluid fill-height class="mb-4 pa-1">
    <v-card flat height="100%" width="100%">
      <v-card-text>
        <TestInstructions class="mb-4" />

        <TestSetupControl :title="$t('tests_setup.title')" :min="0" :max="500" :show-progress="progress"
          :socket-info="socketInfo" @controlUpdated="generateTestNotifications" />

        <NotificationRequestsView :title="$t('notification_requests_view.title')" />

        <NotificationsView v-if="notificationsLoaded" :title="$t('notifications_view.title')"
          :notifications="notifications" @clearNotifications="onClearNotifications" />
      </v-card-text>
    </v-card>

    <SocketConnect />
  </v-container>
</template>

<script>
export default {
  name: "IndexPage",

  data: () => ({
    progress: false,
    notifications: [],

    socketInfo: null
  }),

  computed: {
    notificationsLoaded() {
      return this.notifications && this.notifications.length > 0;
    }
  },

  created() {
    this.$root.$on('onSocketInfoUpdated', (payload) => {
      console.log('Received socket info:', payload)
      this.socketInfo = { ...payload }
    })
  },

  methods: {
    generateTestNotifications(o) {
      const notificationRequests = []
      console.log('Generating tests with parameters: ', o);
      const { testsNumber, strategy, delay } = o;
      for (let ii = 0; ii < testsNumber; ii++) {
        const webId = this.$newWebId()
        const msisdnA = this.getRandomPhone()
        const msisdnB = this.getRandomPhone()
        const n = {
          webId,
          msisdnA,
          msisdnB
        };
        notificationRequests.push(n);
      }
      if (strategy === 'SIM') {
        this.sendSimultaneously(notificationRequests)
      } else if (strategy === 'SEQ') {
        this.sendSequentially(notificationRequests, delay)
      }
    },

    onClearNotifications() {
      this.notifications = [];

      this.$root.$emit('onClearNotificationRequests')
    },

    getRandomPhone() {
      const webId = this.$newWebId()
      return webId.substring(webId.length - 11, webId.length)
    },

    sendSimultaneously(requests) {
      // transform into bunch of promises
    },

    async sendSequentially(requests, delay) {
      for (const r of requests) {
        await this.sendRequest(r)
        await this.$sleep(delay)
      }
    },

    async sendRequest(r) {
      let status = 'OK'
      await this.$axios
        .$post('/unavailableSubscriber', r)
        .catch(e => {
          status = 'FAIL'
        })
      const ret = { ...r, status }
      this.$root.$emit('onAddNotificationRequest', ret)
      return ret
    }
  }
}
</script>
