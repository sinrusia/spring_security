function Universe(){var a;Universe=function(){return a};Universe.prototype=this;a=new Universe;a.constructor=Universe;a.start_time=0;a.bang="Big";return a};function CarMaker(){}CarMaker.prototype.driver=function(){return"Vroom, i have "+this.doors+" doors"};CarMaker.factory=function(a){if("function"!==typeof CarMaker[a])throw{name:"Error",message:a+" dosen't exist"};"function"!==typeof CarMaker[a].prototype.drive&&(CarMaker[a].prototype=new CarMaker);return new CarMaker[a]};CarMaker.Compact=function(){this.doors=4};CarMaker.Convertible=function(){this.doors=2};CarMaker.SUV=function(){this.doors=24};function Sale(a){this.price=a||100}Sale.prototype.getPrice=function(){return this.price};Sale.prototype.decorate=function(a){var d=function(){};a=this.constructor.decorators[a];var b,c;d.prototype=this;c=new d;c.uber=d.prototype;for(b in a)a.hasOwnProperty(b)&&(c[b]=a[b]);return c};Sale.decorators={};Sale.decorators.fedtax={getPrice:function(){var a=this.uber.getPrice();return a+5*a/100}};Sale.decorators.quebec={getPrice:function(){var a=this.uber.getPrice();return a+7.5*a/100}};
Sale.decorators.money={getPrice:function(){return"$"+this.uber.getPrice().toFixed(2)}};Sale.decorators.cdn={getPrice:function(){return"CDN$ "+this.uber.getPrice().toFixed(2)}};