function fun1(ids, factory) {
	// note: the lambda function cannot be removed in some CJS environments
	var deps = ids.map(function(id) {
		return require(id);
	});
	module.exports = factory.apply(null, deps);
}

function fun2(store, meld) {
	"use strict";
	var cache = {};

	// create the module
	meld.around(store, 'get', function(jp) {
			var key = jp.args.join('|');
			return key in cache ? cache[key] : cache[key] = jp.proceed();
		};

		// return your module's exports
		return store;
}

fun1(['./store', 'meld'], fun2);

fun2(null, ['./store', 'meld']);

(function(a) {
	alert(1)
	a(123, function(c, e) {
		alert(3)
		alert(c + '>>>' + e)
	})
}(
	function(d, b) {
		alert(2)
		b(234, d)
	}
));

(function(a) {
	alert(1)
	a(['store', 'meld'], function(c, e) {
		alert(3)
		e.forEach(function(f) {
			c += f + ' , ';
		})
		return c;
	})
}(
	function(d, b) {
		alert(2)
		var deps = d.map(function(id) {
			alert(id);
			return(id);
		});
		alert(b(null, deps))
	}
));