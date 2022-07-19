export default (context) => {
  return new Promise(function (resolve) {
    resolve({
      title: 'NNS UI',

      instructions: {
        title: 'Using this application/form',

        i1: 'Enter number of notifications.',
        i2: 'Select strategy to send requests simultaneously or sequentially.'
      },

      tests_setup: {
        title: 'Test Suite Setup',
        button: 'Generate',
        delay: 'Delay',
        number_of_notifications: 'Number of notifications:',
        strategy: {
          title: 'Send requests:',
          options: {
            simultaneously: 'Simultaneously',
            sequentially: 'Sequentially'  
          }
        }
      },

      notification_requests_view: {
        title: 'Notifications Requests',
      },

      notifications_view: {
        title: 'Notifications in progress',

        clear: 'Clear'
      }
    })
  })
}
