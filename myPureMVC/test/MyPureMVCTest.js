TestCase
(
	'MyPureMVCTest',
	
	{
	
		log: function (message)
		{
			if(window.console && console.log)
				console.log(message);
		}
		,
		testLibrary:function()
		{
			assertSame("messages = ?", 5, 5);
		}
	}
)