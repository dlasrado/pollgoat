
  var PollController = (function() {

    function PollController($log, PollService) {
      this.$log = $log;
      this.PollService = PollService;
      this.$log.debug("constructing PollController");
      this.polls = [];
      this.adpolls = [];
      this.getTop10Polls();
      this.getAdPolls();
    }

    PollController.prototype.getTop10Polls = function() {
      var _this = this;
      this.$log.debug("getTop10Polls()");
      return this.PollService.listTop10Polls().then(function(data) {
        _this.$log.debug("Promise returned " + data.length + " Polls");
        return _this.polls = data;
      }, function(error) {
        return _this.$log.error("Unable to get polls: " + error);
      });
    };
    
    PollController.prototype.getAdPolls = function() {
        var _this = this;
        this.$log.debug("getAdPolls()");
        return this.PollService.listAdPolls().then(function(data) {
          _this.$log.debug("Promise returned " + data.length + " ad Polls");
          return _this.adpolls = data;
        }, function(error) {
          return _this.$log.error("Unable to get ad polls: " + error);
        });
      };

    return PollController;

  })();

  controllersModule.controller('PollController', PollController);

