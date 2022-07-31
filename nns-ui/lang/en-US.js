export default (context) => {
  return new Promise(function (resolve) {
    resolve({
      title: 'NNS UI',

      instructions: {
        title: 'Using this application/form',

        i1: 'Enter number of notifications (1-2000).',
        i2: 'Select strategy to send requests sequentially or simultaneously.'
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

      requests429: {
        title: 'Requests with HTTP response code 429'
      },

      notification_requests_view: {
        title: 'Notifications Requests',
        hide_completed: 'Hide completed',
        item: {
          webId: 'Web Id:',
          sourcePhone: 'Source Phone (msisdnB):',
          destinationPhone: 'Destination Phone (msisdnA):',
          status: 'POST status:',
          messages: {
            title: 'Processing flow:',
            item: {
              comments: 'Comments: '
            }
          }
        },

        clear: 'Clear'
      }
    })
  })
}
