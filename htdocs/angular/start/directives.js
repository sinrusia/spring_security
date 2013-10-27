angular.module('directive', []).directive('contentEditable', function(){
	return {
		require: 'ngModel',
		link: function(scope, elm, attrs, ctrl) {
			elm.bind('blur', function() {
				scope.$apply(function() {
					ctrl.$setViewValue(elm.html());
				});
			});
			
			ctrl.$render = function (value) {
				elm.html(value);
			};
			
			ctrn.$setViewValue(elm.html());
		}
	};
});