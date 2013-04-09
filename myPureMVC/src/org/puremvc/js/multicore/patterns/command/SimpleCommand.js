function SimpleCommand () {};

SimpleCommand.prototype = new Notifier;

SimpleCommand.prototype.constructor = SimpleCommand;

SimpleCommand.prototype.execute = function (notification) {};