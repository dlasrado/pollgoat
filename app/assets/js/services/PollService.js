

  PollService = (function() {

	  PollService.headers = {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    };

	  PollService.defaultConfig = {
      headers: PollService.headers
    };

    function PollService($log, $http, $q) {
      this.$log = $log;
      this.$http = $http;
      this.$q = $q;
      this.$log.debug("constructing PollService");
    }

    PollService.prototype.listTop10Polls = function() {
      var deferred,
        _this = this;
      this.$log.debug("listTop10Polls()");
      deferred = this.$q.defer();
      this.$http.get("/polls").success(function(data, status, headers) {
        _this.$log.info("Successfully listed polls - status " + status);
        return deferred.resolve(data);
      }).error(function(data, status, headers) {
        _this.$log.error("Failed to list polls - status " + status);
        return deferred.reject(data);
      });
      return deferred.promise;
    };
    
    PollService.prototype.listAdPolls = function() {
        var deferred,
          _this = this;
        this.$log.debug("listAdPolls()");
        deferred = this.$q.defer();
        this.$http.get("/polls").success(function(data, status, headers) {
          _this.$log.info("Successfully listed polls - status " + status);
          return deferred.resolve(data);
        }).error(function(data, status, headers) {
          _this.$log.error("Failed to list polls - status " + status);
          return deferred.reject(data);
        });
        return deferred.promise;
      };

    PollService.prototype.upsertPoll = function(poll) {
      var deferred,
        _this = this;
      this.$log.debug("upsertPoll " + (angular.toJson(poll, true)));
      deferred = this.$q.defer();
      this.$http.post('/poll', poll).success(function(data, status, headers) {
        _this.$log.info("Successfully created poll - status " + status);
        return deferred.resolve(data);
      }).error(function(data, status, headers) {
        _this.$log.error("Failed to create poll - status " + status);
        return deferred.reject(data);
      });
      return deferred.promise;
    };

    return PollService;

  })();

  servicesModule.service('PollService', PollService);


