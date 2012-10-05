package opendream.infoaid.job



class NeedTimeboxingJob {
    static triggers = {
      // schedule for everyday at midnight
        cron name: 'needTimeboxingCronTrigger', cronExpression: "0 0 0 * * ?"
    }
    def timeboxingService
    def group = "need"
    def execute() {
        timeboxingService.disableExpiredNeed()
    }
}
