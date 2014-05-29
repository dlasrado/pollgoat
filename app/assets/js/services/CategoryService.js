

  CategoryService = (function() {

	  CategoryService.headers = {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    };

	  CategoryService.defaultConfig = {
      headers: CategoryService.headers
    };

    function CategoryService($log, $http, $q) {
      this.$log = $log;
      this.$http = $http;
      this.$q = $q;
      this.$log.debug("constructing CategoryService");
    }

    CategoryService.prototype.listCategories = function() {
      var deferred,
        _this = this;
      this.$log.debug("listCategories()");
      deferred = this.$q.defer();
      this.$http.get("/categories").success(function(data, status, headers) {
        _this.$log.info("Successfully listed categories - status " + status);
        return deferred.resolve(data);
      }).error(function(data, status, headers) {
        _this.$log.error("Failed to list categories - status " + status);
        return deferred.reject(data);
      });
      return deferred.promise;
    };
    
    

    return CategoryService;

  })();

  servicesModule.service('CategoryService', CategoryService);


