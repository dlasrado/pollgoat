
  var CategoryController = (function() {

    function CategoryController($log, CategoryService) {
      this.$log = $log;
      this.CategoryService = CategoryService;
      this.$log.debug("constructing CategoryController");
      console.log("constructing CategoryController");
      this.categories = [];
      this.getCategories();
    }

    CategoryController.prototype.getCategories = function() {
      var _this = this;
      this.$log.debug("getCategories()");
      return this.PollService.listCategories().then(function(data) {
        _this.$log.debug("Promise returned " + data.length + " Categories");
        return _this.categories = data;
      }, function(error) {
        return _this.$log.error("Unable to get Categories: " + error);
      });
    };

    return CategoryController;

  })();

  controllersModule.controller('CategoryController', CategoryController);
  


