function TodoCtrl($scope) {
	
	alert('start');
	
	$scope.todos = [
	                {text:'learn angular', done:true},
	                {text:'build an angular app', done:false}
	                ];
	
	// addTodo()함수에 바인디오딘다.
	$scope.addTodo = function() {
		$scope.todos.push({text:$scope.todoText, done:false});
		$scope.todoText = '';
	};
	
	//remaining() 함수에 바인딩 된다.
	$scope.remaining = function() {
		var count = 0;
		angular.forEach($scope.todos, function(todo){
			count += todo.done ? 0 : 1;
		});
		return count;
	};
	
	// archive() 함수에 바인딩된다.
	$scope.archive = function() {
		var oldTodos = $scope.todos;
		$scope.todos = [];
		
		angular.forEach(oldTodos, function(Todo) {
			if (!todo.done) $scope.todos.push(todo);
		});
	};
}