function Universe(){var a;Universe=function(){return a};Universe.prototype=this;a=new Universe;a.constructor=Universe;a.start_time=0;a.bang="Big";return a};function CarMaker(){}CarMaker.prototype.driver=function(){return"Vroom, i have "+this.doors+" doors"};CarMaker.factory=function(a){if("function"!==typeof CarMaker[a])throw{name:"Error",message:a+" dosen't exist"};"function"!==typeof CarMaker[a].prototype.drive&&(CarMaker[a].prototype=new CarMaker);return new CarMaker[a]};CarMaker.Compact=function(){this.doors=4};CarMaker.Convertible=function(){this.doors=2};CarMaker.SUV=function(){this.doors=24};function Sale(a){this.price=a||100}Sale.prototype.getPrice=function(){return this.price};Sale.prototype.decorate=function(a){var b=function(){};a=this.constructor.decorators[a];var d,c;b.prototype=this;c=new b;c.uber=b.prototype;for(d in a)a.hasOwnProperty(d)&&(c[d]=a[d]);return c};Sale.decorators={};Sale.decorators.fedtax={getPrice:function(){var a=this.uber.getPrice();return a+5*a/100}};Sale.decorators.quebec={getPrice:function(){var a=this.uber.getPrice();return a+7.5*a/100}};
Sale.decorators.money={getPrice:function(){return"$"+this.uber.getPrice().toFixed(2)}};Sale.decorators.cdn={getPrice:function(){return"CDN$ "+this.uber.getPrice().toFixed(2)}};var publisher={subscribers:{any:[]},subscribe:function(a,b){b=b||"any";"undefined"===typeof this.subscribers[b]&&(this.subscribers[b]=[]);this.subscribers[b].push(a)},unscribe:function(a,b){this.visitSubscribers("unsubscrive",a,b)},publish:function(a,b){this.visitSubscribers("publish",a,b)},visitSubscribers:function(a,b,d){d=this.subscribers[d||"any"];var c,e=d.length;for(c=0;c<e;c+=1)if("publish"===a)d[c](b);else d[c]===b&&d.splice(c,1)}};
function makePublisher(a){for(var b in publisher)b in publisher&&publisher.hasOwnProperty(b)&&"function"===typeof publisher[b]&&(a[b]=publisher[b]);a.subscribers={any:[]}};