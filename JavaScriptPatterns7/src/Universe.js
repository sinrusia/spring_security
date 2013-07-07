function Universe() {
  var instance;
  
  Universe = function () {
    return instance;
  };
  
  Universe.prototype = this;
  
  // instance
  instance = new Universe();
  
  instance.constructor = Universe;
  
  instance.start_time = 0;
  instance.bang = "Big";
  
  return instance;
}
