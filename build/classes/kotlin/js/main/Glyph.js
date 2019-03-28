if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'Glyph'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'Glyph'.");
}
var Glyph = function (_, Kotlin) {
  'use strict';
  var Unit = Kotlin.kotlin.Unit;
  var getCallableRef = Kotlin.getCallableRef;
  var Kind_INTERFACE = Kotlin.Kind.INTERFACE;
  var removeAll = Kotlin.kotlin.collections.removeAll_uhyeqt$;
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var MutableCollection = Kotlin.kotlin.collections.MutableCollection;
  var Any = Object;
  var throwCCE = Kotlin.throwCCE;
  function dispose$lambda() {
    return Unit;
  }
  var dispose;
  function dispose_0(value) {
    return value;
  }
  function dispose$lambda$lambda(closure$l, closure$r) {
    return function () {
      closure$l();
      closure$r();
      return Unit;
    };
  }
  var UnsupportedOperationException_init = Kotlin.kotlin.UnsupportedOperationException_init_pdl1vj$;
  var get_lastIndex = Kotlin.kotlin.collections.get_lastIndex_m7z4lg$;
  function dispose_1(values) {
    var tmp$;
    if (values.length === 0)
      throw UnsupportedOperationException_init("Empty array can't be reduced.");
    var accumulator = values[0];
    tmp$ = get_lastIndex(values);
    for (var index = 1; index <= tmp$; index++) {
      accumulator = dispose$lambda$lambda(accumulator, values[index]);
    }
    return accumulator;
  }
  function Disposable() {
  }
  Disposable.prototype.toDispose = function () {
    return getCallableRef('dispose', function ($receiver) {
      return $receiver.dispose(), Unit;
    }.bind(null, this));
  };
  Disposable.$metadata$ = {
    kind: Kind_INTERFACE,
    simpleName: 'Disposable',
    interfaces: []
  };
  var ArrayList_init = Kotlin.kotlin.collections.ArrayList_init_287e2$;
  function DisposableCollection(collection) {
    if (collection === void 0) {
      collection = ArrayList_init();
    }
    this.collection_0 = collection;
  }
  function DisposableCollection$dispose$lambda(dispose) {
    dispose();
    return true;
  }
  DisposableCollection.prototype.dispose = function () {
    removeAll(this.collection_0, DisposableCollection$dispose$lambda);
  };
  Object.defineProperty(DisposableCollection.prototype, 'size', {
    get: function () {
      return this.collection_0.size;
    }
  });
  DisposableCollection.prototype.add_11rb$ = function (element) {
    return this.collection_0.add_11rb$(element);
  };
  DisposableCollection.prototype.addAll_brywnq$ = function (elements) {
    return this.collection_0.addAll_brywnq$(elements);
  };
  DisposableCollection.prototype.clear = function () {
    return this.collection_0.clear();
  };
  DisposableCollection.prototype.contains_11rb$ = function (element) {
    return this.collection_0.contains_11rb$(element);
  };
  DisposableCollection.prototype.containsAll_brywnq$ = function (elements) {
    return this.collection_0.containsAll_brywnq$(elements);
  };
  DisposableCollection.prototype.isEmpty = function () {
    return this.collection_0.isEmpty();
  };
  DisposableCollection.prototype.iterator = function () {
    return this.collection_0.iterator();
  };
  DisposableCollection.prototype.remove_11rb$ = function (element) {
    return this.collection_0.remove_11rb$(element);
  };
  DisposableCollection.prototype.removeAll_brywnq$ = function (elements) {
    return this.collection_0.removeAll_brywnq$(elements);
  };
  DisposableCollection.prototype.retainAll_brywnq$ = function (elements) {
    return this.collection_0.retainAll_brywnq$(elements);
  };
  DisposableCollection.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'DisposableCollection',
    interfaces: [Disposable, MutableCollection]
  };
  function glyph$lambda(closure$block) {
    return function (scope, bind) {
      return closure$block(scope, bind);
    };
  }
  function glyph(block) {
    return glyph$lambda(block);
  }
  function glyph$lambda_0(closure$block) {
    return function (scope, f) {
      return closure$block(scope);
    };
  }
  function glyph_0(block) {
    return glyph$lambda_0(block);
  }
  function Scope(dependencies, parent, sender, binder) {
    this.dependencies = dependencies;
    this.parent = parent;
    this.sender_0 = sender;
    this.binder_0 = binder;
  }
  Scope.prototype.unaryPlus_wikn$ = function ($receiver) {
    return this.plus_11rc$($receiver);
  };
  Scope.prototype.plus_11rc$ = function (parent) {
    return new Scope(this.dependencies, parent, this.sender_0, this.binder_0);
  };
  Scope.prototype.unaryPlus_gmr32k$ = function ($receiver) {
    return this.plus_42zdrj$($receiver);
  };
  Scope.prototype.plus_42zdrj$ = function (composer) {
    return new Scope(this.dependencies, this.parent, this.sender_0, compose(this.sender_0, composer));
  };
  Scope.prototype.unaryPlus_rvuxmj$ = function ($receiver) {
    return this.plus_wdrsk$($receiver);
  };
  function Scope$plus$lambda$lambda(closure$disposables, this$Scope) {
    return function (receiver) {
      var $receiver = closure$disposables;
      var element = this$Scope.binder_0(receiver);
      $receiver.add_11rb$(element);
      return Unit;
    };
  }
  Scope.prototype.plus_wdrsk$ = function (glyph) {
    var $receiver = new DisposableCollection();
    var element = glyph(this, Scope$plus$lambda$lambda($receiver, this));
    $receiver.add_11rb$(element);
    return $receiver.toDispose();
  };
  Scope.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Scope',
    interfaces: []
  };
  function compose$lambda(closure$composer, this$compose) {
    return function (receiver) {
      return closure$composer(this$compose)(receiver);
    };
  }
  function compose($receiver, composer) {
    return compose$lambda(composer, $receiver);
  }
  function map$lambda(closure$transform) {
    return function (item, receiver) {
      receiver(closure$transform(item));
      return Unit;
    };
  }
  function map($receiver, transform) {
    return wrap($receiver, map$lambda(transform));
  }
  function flatMap$lambda(closure$transform) {
    return function (item, receiver) {
      var tmp$;
      tmp$ = closure$transform(item).iterator();
      while (tmp$.hasNext()) {
        var element = tmp$.next();
        receiver(element);
      }
      return Unit;
    };
  }
  function flatMap($receiver, transform) {
    return wrap($receiver, flatMap$lambda(transform));
  }
  function filter$lambda(closure$predicate) {
    return function (item, receiver) {
      var tmp$;
      if ((tmp$ = closure$predicate(item) ? item : null) != null) {
        receiver(tmp$);
      }
      return Unit;
    };
  }
  function filter($receiver, predicate) {
    return wrap($receiver, filter$lambda(predicate));
  }
  function cast$lambda(item, receiver) {
    var tmp$;
    receiver((tmp$ = item) == null || Kotlin.isType(tmp$, Any) ? tmp$ : throwCCE());
    return Unit;
  }
  function cast($receiver) {
    return wrap($receiver, cast$lambda);
  }
  function distinct$lambda(l, r) {
    return l !== r;
  }
  function distinct$lambda$lambda$lambda(closure$item) {
    return function () {
      return closure$item;
    };
  }
  function distinct$lambda_0(closure$getter, closure$comparator) {
    return function (item, receiver) {
      var tmp$;
      var $receiver = closure$getter.v;
      closure$getter.v = distinct$lambda$lambda$lambda(item);
      var closure$comparator_0 = closure$comparator;
      var block$result;
      var state = $receiver != null ? $receiver() : null;
      if (state == null) {
        block$result = item;
      }
       else if (closure$comparator_0(state, state)) {
        block$result = item;
      }
       else {
        block$result = null;
      }
      if ((tmp$ = block$result) != null) {
        receiver(tmp$);
      }
      return Unit;
    };
  }
  function distinct($receiver, comparator) {
    if (comparator === void 0)
      comparator = distinct$lambda;
    var getter = {v: null};
    return wrap($receiver, distinct$lambda_0(getter, comparator));
  }
  function prepend$lambda(closure$value) {
    return function () {
      return closure$value;
    };
  }
  function prepend($receiver, value) {
    return prepend_0($receiver, prepend$lambda(value));
  }
  function prepend$lambda_0(closure$get, this$prepend) {
    return function (receiver) {
      receiver(closure$get());
      return this$prepend(receiver);
    };
  }
  function prepend_0($receiver, get) {
    return prepend$lambda_0(get, $receiver);
  }
  function before$lambda(closure$block) {
    return function (item, receiver) {
      closure$block(item);
      receiver(item);
      return Unit;
    };
  }
  function before($receiver, block) {
    return wrap($receiver, before$lambda(block));
  }
  function after$lambda(closure$block) {
    return function (item, receiver) {
      receiver(item);
      closure$block(item);
      return Unit;
    };
  }
  function after($receiver, block) {
    return wrap($receiver, after$lambda(block));
  }
  function wrap$lambda$lambda(closure$block, closure$receiver) {
    return function (item) {
      closure$block(item, closure$receiver);
      return Unit;
    };
  }
  function wrap$lambda(this$wrap, closure$block) {
    return function (receiver) {
      return this$wrap(wrap$lambda$lambda(closure$block, receiver));
    };
  }
  function wrap($receiver, block) {
    return wrap$lambda($receiver, block);
  }
  function combine$lambda$lambda$lambda$lambda(closure$l) {
    return function () {
      return closure$l;
    };
  }
  function combine$lambda$lambda$lambda(closure$getLeft, closure$getRight, closure$transform, closure$receiver) {
    return function (l) {
      var tmp$, tmp$_0, tmp$_1;
      closure$getLeft.v = combine$lambda$lambda$lambda$lambda(l);
      var tmp$_2;
      tmp$_2 = (tmp$_0 = (tmp$ = closure$getRight.v) != null ? tmp$() : null) != null ? closure$transform(l, tmp$_0) : null;
      if ((tmp$_1 = tmp$_2) != null) {
        closure$receiver(tmp$_1);
      }
      return Unit;
    };
  }
  function combine$lambda$lambda$lambda$lambda_0(closure$r) {
    return function () {
      return closure$r;
    };
  }
  function combine$lambda$lambda$lambda_0(closure$getRight, closure$getLeft, closure$transform, closure$receiver) {
    return function (r) {
      var tmp$, tmp$_0, tmp$_1;
      closure$getRight.v = combine$lambda$lambda$lambda$lambda_0(r);
      var tmp$_2;
      tmp$_2 = (tmp$_0 = (tmp$ = closure$getLeft.v) != null ? tmp$() : null) != null ? closure$transform(tmp$_0, r) : null;
      if ((tmp$_1 = tmp$_2) != null) {
        closure$receiver(tmp$_1);
      }
      return Unit;
    };
  }
  function combine$lambda$lambda(closure$left, closure$transform, closure$right) {
    return function (receiver) {
      var getLeft = {v: null};
      var getRight = {v: null};
      return dispose_1([closure$left(combine$lambda$lambda$lambda(getLeft, getRight, closure$transform, receiver)), closure$right(combine$lambda$lambda$lambda_0(getRight, getLeft, closure$transform, receiver))]);
    };
  }
  function combine($receiver, right, transform) {
    return combine$lambda$lambda($receiver, transform, right);
  }
  var package$io = _.io || (_.io = {});
  var package$lamart = package$io.lamart || (package$io.lamart = {});
  var package$glyph = package$lamart.glyph || (package$lamart.glyph = {});
  Object.defineProperty(package$glyph, 'dispose', {
    get: function () {
      return dispose;
    }
  });
  package$glyph.dispose_o14v8n$ = dispose_0;
  package$glyph.dispose_hh60ni$ = dispose_1;
  package$glyph.Disposable = Disposable;
  package$glyph.DisposableCollection = DisposableCollection;
  package$glyph.glyph_fuvho7$ = glyph;
  package$glyph.glyph_igsxjk$ = glyph_0;
  package$glyph.Scope = Scope;
  package$glyph.compose_spetp5$ = compose;
  package$glyph.map_mtg55h$ = map;
  package$glyph.flatMap_3mwuku$ = flatMap;
  package$glyph.filter_9847x0$ = filter;
  package$glyph.cast_q6nl8$ = cast;
  package$glyph.distinct_4fa6wy$ = distinct;
  package$glyph.prepend_otmbjz$ = prepend;
  package$glyph.prepend_gig6ln$ = prepend_0;
  package$glyph.before_ubid8w$ = before;
  package$glyph.after_ubid8w$ = after;
  package$glyph.wrap_1mxotp$ = wrap;
  package$glyph.combine_cz066m$ = combine;
  DisposableCollection.prototype.toDispose = Disposable.prototype.toDispose;
  dispose = dispose$lambda;
  Kotlin.defineModule('Glyph', _);
  return _;
}(typeof Glyph === 'undefined' ? {} : Glyph, kotlin);
