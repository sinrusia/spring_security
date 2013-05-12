 Save This PageHome » openjdk-7 » java » lang » reflect » [javadoc | source]
    1   /*
    2    * Copyright (c) 1999, 2010, Oracle and/or its affiliates. All rights reserved.
    3    * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
    4    *
    5    * This code is free software; you can redistribute it and/or modify it
    6    * under the terms of the GNU General Public License version 2 only, as
    7    * published by the Free Software Foundation.  Oracle designates this
    8    * particular file as subject to the "Classpath" exception as provided
    9    * by Oracle in the LICENSE file that accompanied this code.
   10    *
   11    * This code is distributed in the hope that it will be useful, but WITHOUT
   12    * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
   13    * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
   14    * version 2 for more details (a copy is included in the LICENSE file that
   15    * accompanied this code).
   16    *
   17    * You should have received a copy of the GNU General Public License version
   18    * 2 along with this work; if not, write to the Free Software Foundation,
   19    * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
   20    *
   21    * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
   22    * or visit www.oracle.com if you need additional information or have any
   23    * questions.
   24    */
   25   
   26   package java.lang.reflect;
   27   
   28   import java.lang.ref.Reference;
   29   import java.lang.ref.WeakReference;
   30   import java.util.Arrays;
   31   import java.util.Collections;
   32   import java.util.HashMap;
   33   import java.util.HashSet;
   34   import java.util.Map;
   35   import java.util.Set;
   36   import java.util.List;
   37   import java.util.WeakHashMap;
   38   import sun.misc.ProxyGenerator;
   39   
   40   /**
   41    * {@code Proxy} provides static methods for creating dynamic proxy
   42    * classes and instances, and it is also the superclass of all
   43    * dynamic proxy classes created by those methods.
   44    *
   45    * <p>To create a proxy for some interface {@code Foo}:
   46    * <pre>
   47    *     InvocationHandler handler = new MyInvocationHandler(...);
   48    *     Class proxyClass = Proxy.getProxyClass(
   49    *         Foo.class.getClassLoader(), new Class[] { Foo.class });
   50    *     Foo f = (Foo) proxyClass.
   51    *         getConstructor(new Class[] { InvocationHandler.class }).
   52    *         newInstance(new Object[] { handler });
   53    * </pre>
   54    * or more simply:
   55    * <pre>
   56    *     Foo f = (Foo) Proxy.newProxyInstance(Foo.class.getClassLoader(),
   57    *                                          new Class[] { Foo.class },
   58    *                                          handler);
   59    * </pre>
   60    *
   61    * <p>A <i>dynamic proxy class</i> (simply referred to as a <i>proxy
   62    * class</i> below) is a class that implements a list of interfaces
   63    * specified at runtime when the class is created, with behavior as
   64    * described below.
   65    *
   66    * A <i>proxy interface</i> is such an interface that is implemented
   67    * by a proxy class.
   68    *
   69    * A <i>proxy instance</i> is an instance of a proxy class.
   70    *
   71    * Each proxy instance has an associated <i>invocation handler</i>
   72    * object, which implements the interface {@link InvocationHandler}.
   73    * A method invocation on a proxy instance through one of its proxy
   74    * interfaces will be dispatched to the {@link InvocationHandler#invoke
   75    * invoke} method of the instance's invocation handler, passing the proxy
   76    * instance, a {@code java.lang.reflect.Method} object identifying
   77    * the method that was invoked, and an array of type {@code Object}
   78    * containing the arguments.  The invocation handler processes the
   79    * encoded method invocation as appropriate and the result that it
   80    * returns will be returned as the result of the method invocation on
   81    * the proxy instance.
   82    *
   83    * <p>A proxy class has the following properties:
   84    *
   85    * <ul>
   86    * <li>Proxy classes are public, final, and not abstract.
   87    *
   88    * <li>The unqualified name of a proxy class is unspecified.  The space
   89    * of class names that begin with the string {@code "$Proxy"}
   90    * should be, however, reserved for proxy classes.
   91    *
   92    * <li>A proxy class extends {@code java.lang.reflect.Proxy}.
   93    *
   94    * <li>A proxy class implements exactly the interfaces specified at its
   95    * creation, in the same order.
   96    *
   97    * <li>If a proxy class implements a non-public interface, then it will
   98    * be defined in the same package as that interface.  Otherwise, the
   99    * package of a proxy class is also unspecified.  Note that package
  100    * sealing will not prevent a proxy class from being successfully defined
  101    * in a particular package at runtime, and neither will classes already
  102    * defined by the same class loader and the same package with particular
  103    * signers.
  104    *
  105    * <li>Since a proxy class implements all of the interfaces specified at
  106    * its creation, invoking {@code getInterfaces} on its
  107    * {@code Class} object will return an array containing the same
  108    * list of interfaces (in the order specified at its creation), invoking
  109    * {@code getMethods} on its {@code Class} object will return
  110    * an array of {@code Method} objects that include all of the
  111    * methods in those interfaces, and invoking {@code getMethod} will
  112    * find methods in the proxy interfaces as would be expected.
  113    *
  114    * <li>The {@link Proxy#isProxyClass Proxy.isProxyClass} method will
  115    * return true if it is passed a proxy class-- a class returned by
  116    * {@code Proxy.getProxyClass} or the class of an object returned by
  117    * {@code Proxy.newProxyInstance}-- and false otherwise.
  118    *
  119    * <li>The {@code java.security.ProtectionDomain} of a proxy class
  120    * is the same as that of system classes loaded by the bootstrap class
  121    * loader, such as {@code java.lang.Object}, because the code for a
  122    * proxy class is generated by trusted system code.  This protection
  123    * domain will typically be granted
  124    * {@code java.security.AllPermission}.
  125    *
  126    * <li>Each proxy class has one public constructor that takes one argument,
  127    * an implementation of the interface {@link InvocationHandler}, to set
  128    * the invocation handler for a proxy instance.  Rather than having to use
  129    * the reflection API to access the public constructor, a proxy instance
  130    * can be also be created by calling the {@link Proxy#newProxyInstance
  131    * Proxy.newProxyInstance} method, which combines the actions of calling
  132    * {@link Proxy#getProxyClass Proxy.getProxyClass} with invoking the
  133    * constructor with an invocation handler.
  134    * </ul>
  135    *
  136    * <p>A proxy instance has the following properties:
  137    *
  138    * <ul>
  139    * <li>Given a proxy instance {@code proxy} and one of the
  140    * interfaces implemented by its proxy class {@code Foo}, the
  141    * following expression will return true:
  142    * <pre>
  143    *     {@code proxy instanceof Foo}
  144    * </pre>
  145    * and the following cast operation will succeed (rather than throwing
  146    * a {@code ClassCastException}):
  147    * <pre>
  148    *     {@code (Foo) proxy}
  149    * </pre>
  150    *
  151    * <li>Each proxy instance has an associated invocation handler, the one
  152    * that was passed to its constructor.  The static
  153    * {@link Proxy#getInvocationHandler Proxy.getInvocationHandler} method
  154    * will return the invocation handler associated with the proxy instance
  155    * passed as its argument.
  156    *
  157    * <li>An interface method invocation on a proxy instance will be
  158    * encoded and dispatched to the invocation handler's {@link
  159    * InvocationHandler#invoke invoke} method as described in the
  160    * documentation for that method.
  161    *
  162    * <li>An invocation of the {@code hashCode},
  163    * {@code equals}, or {@code toString} methods declared in
  164    * {@code java.lang.Object} on a proxy instance will be encoded and
  165    * dispatched to the invocation handler's {@code invoke} method in
  166    * the same manner as interface method invocations are encoded and
  167    * dispatched, as described above.  The declaring class of the
  168    * {@code Method} object passed to {@code invoke} will be
  169    * {@code java.lang.Object}.  Other public methods of a proxy
  170    * instance inherited from {@code java.lang.Object} are not
  171    * overridden by a proxy class, so invocations of those methods behave
  172    * like they do for instances of {@code java.lang.Object}.
  173    * </ul>
  174    *
  175    * <h3>Methods Duplicated in Multiple Proxy Interfaces</h3>
  176    *
  177    * <p>When two or more interfaces of a proxy class contain a method with
  178    * the same name and parameter signature, the order of the proxy class's
  179    * interfaces becomes significant.  When such a <i>duplicate method</i>
  180    * is invoked on a proxy instance, the {@code Method} object passed
  181    * to the invocation handler will not necessarily be the one whose
  182    * declaring class is assignable from the reference type of the interface
  183    * that the proxy's method was invoked through.  This limitation exists
  184    * because the corresponding method implementation in the generated proxy
  185    * class cannot determine which interface it was invoked through.
  186    * Therefore, when a duplicate method is invoked on a proxy instance,
  187    * the {@code Method} object for the method in the foremost interface
  188    * that contains the method (either directly or inherited through a
  189    * superinterface) in the proxy class's list of interfaces is passed to
  190    * the invocation handler's {@code invoke} method, regardless of the
  191    * reference type through which the method invocation occurred.
  192    *
  193    * <p>If a proxy interface contains a method with the same name and
  194    * parameter signature as the {@code hashCode}, {@code equals},
  195    * or {@code toString} methods of {@code java.lang.Object},
  196    * when such a method is invoked on a proxy instance, the
  197    * {@code Method} object passed to the invocation handler will have
  198    * {@code java.lang.Object} as its declaring class.  In other words,
  199    * the public, non-final methods of {@code java.lang.Object}
  200    * logically precede all of the proxy interfaces for the determination of
  201    * which {@code Method} object to pass to the invocation handler.
  202    *
  203    * <p>Note also that when a duplicate method is dispatched to an
  204    * invocation handler, the {@code invoke} method may only throw
  205    * checked exception types that are assignable to one of the exception
  206    * types in the {@code throws} clause of the method in <i>all</i> of
  207    * the proxy interfaces that it can be invoked through.  If the
  208    * {@code invoke} method throws a checked exception that is not
  209    * assignable to any of the exception types declared by the method in one
  210    * of the proxy interfaces that it can be invoked through, then an
  211    * unchecked {@code UndeclaredThrowableException} will be thrown by
  212    * the invocation on the proxy instance.  This restriction means that not
  213    * all of the exception types returned by invoking
  214    * {@code getExceptionTypes} on the {@code Method} object
  215    * passed to the {@code invoke} method can necessarily be thrown
  216    * successfully by the {@code invoke} method.
  217    *
  218    * @author      Peter Jones
  219    * @see         InvocationHandler
  220    * @since       1.3
  221    */
  222   public class Proxy implements java.io.Serializable {
  223   
  224       private static final long serialVersionUID = -2222568056686623797L;
  225   
  226       /** prefix for all proxy class names */
  227       private final static String proxyClassNamePrefix = "$Proxy";
  228   
  229       /** parameter types of a proxy class constructor */
  230       private final static Class[] constructorParams =
  231           { InvocationHandler.class };
  232   
  233       /** maps a class loader to the proxy class cache for that loader */
  234       private static Map<ClassLoader, Map<List<String>, Object>> loaderToCache
  235           = new WeakHashMap<>();
  236   
  237       /** marks that a particular proxy class is currently being generated */
  238       private static Object pendingGenerationMarker = new Object();
  239   
  240       /** next number to use for generation of unique proxy class names */
  241       private static long nextUniqueNumber = 0;
  242       private static Object nextUniqueNumberLock = new Object();
  243   
  244       /** set of all generated proxy classes, for isProxyClass implementation */
  245       private static Map<Class<?>, Void> proxyClasses =
  246           Collections.synchronizedMap(new WeakHashMap<Class<?>, Void>());
  247   
  248       /**
  249        * the invocation handler for this proxy instance.
  250        * @serial
  251        */
  252       protected InvocationHandler h;
  253   
  254       /**
  255        * Prohibits instantiation.
  256        */
  257       private Proxy() {
  258       }
  259   
  260       /**
  261        * Constructs a new {@code Proxy} instance from a subclass
  262        * (typically, a dynamic proxy class) with the specified value
  263        * for its invocation handler.
  264        *
  265        * @param   h the invocation handler for this proxy instance
  266        */
  267       protected Proxy(InvocationHandler h) {
  268           this.h = h;
  269       }
  270   
  271       /**
  272        * Returns the {@code java.lang.Class} object for a proxy class
  273        * given a class loader and an array of interfaces.  The proxy class
  274        * will be defined by the specified class loader and will implement
  275        * all of the supplied interfaces.  If a proxy class for the same
  276        * permutation of interfaces has already been defined by the class
  277        * loader, then the existing proxy class will be returned; otherwise,
  278        * a proxy class for those interfaces will be generated dynamically
  279        * and defined by the class loader.
  280        *
  281        * <p>There are several restrictions on the parameters that may be
  282        * passed to {@code Proxy.getProxyClass}:
  283        *
  284        * <ul>
  285        * <li>All of the {@code Class} objects in the
  286        * {@code interfaces} array must represent interfaces, not
  287        * classes or primitive types.
  288        *
  289        * <li>No two elements in the {@code interfaces} array may
  290        * refer to identical {@code Class} objects.
  291        *
  292        * <li>All of the interface types must be visible by name through the
  293        * specified class loader.  In other words, for class loader
  294        * {@code cl} and every interface {@code i}, the following
  295        * expression must be true:
  296        * <pre>
  297        *     Class.forName(i.getName(), false, cl) == i
  298        * </pre>
  299        *
  300        * <li>All non-public interfaces must be in the same package;
  301        * otherwise, it would not be possible for the proxy class to
  302        * implement all of the interfaces, regardless of what package it is
  303        * defined in.
  304        *
  305        * <li>For any set of member methods of the specified interfaces
  306        * that have the same signature:
  307        * <ul>
  308        * <li>If the return type of any of the methods is a primitive
  309        * type or void, then all of the methods must have that same
  310        * return type.
  311        * <li>Otherwise, one of the methods must have a return type that
  312        * is assignable to all of the return types of the rest of the
  313        * methods.
  314        * </ul>
  315        *
  316        * <li>The resulting proxy class must not exceed any limits imposed
  317        * on classes by the virtual machine.  For example, the VM may limit
  318        * the number of interfaces that a class may implement to 65535; in
  319        * that case, the size of the {@code interfaces} array must not
  320        * exceed 65535.
  321        * </ul>
  322        *
  323        * <p>If any of these restrictions are violated,
  324        * {@code Proxy.getProxyClass} will throw an
  325        * {@code IllegalArgumentException}.  If the {@code interfaces}
  326        * array argument or any of its elements are {@code null}, a
  327        * {@code NullPointerException} will be thrown.
  328        *
  329        * <p>Note that the order of the specified proxy interfaces is
  330        * significant: two requests for a proxy class with the same combination
  331        * of interfaces but in a different order will result in two distinct
  332        * proxy classes.
  333        *
  334        * @param   loader the class loader to define the proxy class
  335        * @param   interfaces the list of interfaces for the proxy class
  336        *          to implement
  337        * @return  a proxy class that is defined in the specified class loader
  338        *          and that implements the specified interfaces
  339        * @throws  IllegalArgumentException if any of the restrictions on the
  340        *          parameters that may be passed to {@code getProxyClass}
  341        *          are violated
  342        * @throws  NullPointerException if the {@code interfaces} array
  343        *          argument or any of its elements are {@code null}
  344        */
  345       public static Class<?> getProxyClass(ClassLoader loader,
  346                                            Class<?>... interfaces)
  347           throws IllegalArgumentException
  348       {
  349           if (interfaces.length > 65535) {
  350               throw new IllegalArgumentException("interface limit exceeded");
  351           }
  352   
  353           Class<?> proxyClass = null;
  354   
  355           /* collect interface names to use as key for proxy class cache */
  356           String[] interfaceNames = new String[interfaces.length];
  357   
  358           // for detecting duplicates
  359           Set<Class<?>> interfaceSet = new HashSet<>();
  360   
  361           for (int i = 0; i < interfaces.length; i++) {
  362               /*
  363                * Verify that the class loader resolves the name of this
  364                * interface to the same Class object.
  365                */
  366               String interfaceName = interfaces[i].getName();
  367               Class<?> interfaceClass = null;
  368               try {
  369                   interfaceClass = Class.forName(interfaceName, false, loader);
  370               } catch (ClassNotFoundException e) {
  371               }
  372               if (interfaceClass != interfaces[i]) {
  373                   throw new IllegalArgumentException(
  374                       interfaces[i] + " is not visible from class loader");
  375               }
  376   
  377               /*
  378                * Verify that the Class object actually represents an
  379                * interface.
  380                */
  381               if (!interfaceClass.isInterface()) {
  382                   throw new IllegalArgumentException(
  383                       interfaceClass.getName() + " is not an interface");
  384               }
  385   
  386               /*
  387                * Verify that this interface is not a duplicate.
  388                */
  389               if (interfaceSet.contains(interfaceClass)) {
  390                   throw new IllegalArgumentException(
  391                       "repeated interface: " + interfaceClass.getName());
  392               }
  393               interfaceSet.add(interfaceClass);
  394   
  395               interfaceNames[i] = interfaceName;
  396           }
  397   
  398           /*
  399            * Using string representations of the proxy interfaces as
  400            * keys in the proxy class cache (instead of their Class
  401            * objects) is sufficient because we require the proxy
  402            * interfaces to be resolvable by name through the supplied
  403            * class loader, and it has the advantage that using a string
  404            * representation of a class makes for an implicit weak
  405            * reference to the class.
  406            */
  407           List<String> key = Arrays.asList(interfaceNames);
  408   
  409           /*
  410            * Find or create the proxy class cache for the class loader.
  411            */
  412           Map<List<String>, Object> cache;
  413           synchronized (loaderToCache) {
  414               cache = loaderToCache.get(loader);
  415               if (cache == null) {
  416                   cache = new HashMap<>();
  417                   loaderToCache.put(loader, cache);
  418               }
  419               /*
  420                * This mapping will remain valid for the duration of this
  421                * method, without further synchronization, because the mapping
  422                * will only be removed if the class loader becomes unreachable.
  423                */
  424           }
  425   
  426           /*
  427            * Look up the list of interfaces in the proxy class cache using
  428            * the key.  This lookup will result in one of three possible
  429            * kinds of values:
  430            *     null, if there is currently no proxy class for the list of
  431            *         interfaces in the class loader,
  432            *     the pendingGenerationMarker object, if a proxy class for the
  433            *         list of interfaces is currently being generated,
  434            *     or a weak reference to a Class object, if a proxy class for
  435            *         the list of interfaces has already been generated.
  436            */
  437           synchronized (cache) {
  438               /*
  439                * Note that we need not worry about reaping the cache for
  440                * entries with cleared weak references because if a proxy class
  441                * has been garbage collected, its class loader will have been
  442                * garbage collected as well, so the entire cache will be reaped
  443                * from the loaderToCache map.
  444                */
  445               do {
  446                   Object value = cache.get(key);
  447                   if (value instanceof Reference) {
  448                       proxyClass = (Class<?>) ((Reference) value).get();
  449                   }
  450                   if (proxyClass != null) {
  451                       // proxy class already generated: return it
  452                       return proxyClass;
  453                   } else if (value == pendingGenerationMarker) {
  454                       // proxy class being generated: wait for it
  455                       try {
  456                           cache.wait();
  457                       } catch (InterruptedException e) {
  458                           /*
  459                            * The class generation that we are waiting for should
  460                            * take a small, bounded time, so we can safely ignore
  461                            * thread interrupts here.
  462                            */
  463                       }
  464                       continue;
  465                   } else {
  466                       /*
  467                        * No proxy class for this list of interfaces has been
  468                        * generated or is being generated, so we will go and
  469                        * generate it now.  Mark it as pending generation.
  470                        */
  471                       cache.put(key, pendingGenerationMarker);
  472                       break;
  473                   }
  474               } while (true);
  475           }
  476   
  477           try {
  478               String proxyPkg = null;     // package to define proxy class in
  479   
  480               /*
  481                * Record the package of a non-public proxy interface so that the
  482                * proxy class will be defined in the same package.  Verify that
  483                * all non-public proxy interfaces are in the same package.
  484                */
  485               for (int i = 0; i < interfaces.length; i++) {
  486                   int flags = interfaces[i].getModifiers();
  487                   if (!Modifier.isPublic(flags)) {
  488                       String name = interfaces[i].getName();
  489                       int n = name.lastIndexOf('.');
  490                       String pkg = ((n == -1) ? "" : name.substring(0, n + 1));
  491                       if (proxyPkg == null) {
  492                           proxyPkg = pkg;
  493                       } else if (!pkg.equals(proxyPkg)) {
  494                           throw new IllegalArgumentException(
  495                               "non-public interfaces from different packages");
  496                       }
  497                   }
  498               }
  499   
  500               if (proxyPkg == null) {     // if no non-public proxy interfaces,
  501                   proxyPkg = "";          // use the unnamed package
  502               }
  503   
  504               {
  505                   /*
  506                    * Choose a name for the proxy class to generate.
  507                    */
  508                   long num;
  509                   synchronized (nextUniqueNumberLock) {
  510                       num = nextUniqueNumber++;
  511                   }
  512                   String proxyName = proxyPkg + proxyClassNamePrefix + num;
  513                   /*
  514                    * Verify that the class loader hasn't already
  515                    * defined a class with the chosen name.
  516                    */
  517   
  518                   /*
  519                    * Generate the specified proxy class.
  520                    */
  521                   byte[] proxyClassFile = ProxyGenerator.generateProxyClass(
  522                       proxyName, interfaces);
  523                   try {
  524                       proxyClass = defineClass0(loader, proxyName,
  525                           proxyClassFile, 0, proxyClassFile.length);
  526                   } catch (ClassFormatError e) {
  527                       /*
  528                        * A ClassFormatError here means that (barring bugs in the
  529                        * proxy class generation code) there was some other
  530                        * invalid aspect of the arguments supplied to the proxy
  531                        * class creation (such as virtual machine limitations
  532                        * exceeded).
  533                        */
  534                       throw new IllegalArgumentException(e.toString());
  535                   }
  536               }
  537               // add to set of all generated proxy classes, for isProxyClass
  538               proxyClasses.put(proxyClass, null);
  539   
  540           } finally {
  541               /*
  542                * We must clean up the "pending generation" state of the proxy
  543                * class cache entry somehow.  If a proxy class was successfully
  544                * generated, store it in the cache (with a weak reference);
  545                * otherwise, remove the reserved entry.  In all cases, notify
  546                * all waiters on reserved entries in this cache.
  547                */
  548               synchronized (cache) {
  549                   if (proxyClass != null) {
  550                       cache.put(key, new WeakReference<Class<?>>(proxyClass));
  551                   } else {
  552                       cache.remove(key);
  553                   }
  554                   cache.notifyAll();
  555               }
  556           }
  557           return proxyClass;
  558       }
  559   
  560       /**
  561        * Returns an instance of a proxy class for the specified interfaces
  562        * that dispatches method invocations to the specified invocation
  563        * handler.  This method is equivalent to:
  564        * <pre>
  565        *     Proxy.getProxyClass(loader, interfaces).
  566        *         getConstructor(new Class[] { InvocationHandler.class }).
  567        *         newInstance(new Object[] { handler });
  568        * </pre>
  569        *
  570        * <p>{@code Proxy.newProxyInstance} throws
  571        * {@code IllegalArgumentException} for the same reasons that
  572        * {@code Proxy.getProxyClass} does.
  573        *
  574        * @param   loader the class loader to define the proxy class
  575        * @param   interfaces the list of interfaces for the proxy class
  576        *          to implement
  577        * @param   h the invocation handler to dispatch method invocations to
  578        * @return  a proxy instance with the specified invocation handler of a
  579        *          proxy class that is defined by the specified class loader
  580        *          and that implements the specified interfaces
  581        * @throws  IllegalArgumentException if any of the restrictions on the
  582        *          parameters that may be passed to {@code getProxyClass}
  583        *          are violated
  584        * @throws  NullPointerException if the {@code interfaces} array
  585        *          argument or any of its elements are {@code null}, or
  586        *          if the invocation handler, {@code h}, is
  587        *          {@code null}
  588        */
  589       public static Object newProxyInstance(ClassLoader loader,
  590                                             Class<?>[] interfaces,
  591                                             InvocationHandler h)
  592           throws IllegalArgumentException
  593       {
  594           if (h == null) {
  595               throw new NullPointerException();
  596           }
  597   
  598           /*
  599            * Look up or generate the designated proxy class.
  600            */
  601           Class<?> cl = getProxyClass(loader, interfaces);
  602   
  603           /*
  604            * Invoke its constructor with the designated invocation handler.
  605            */
  606           try {
  607               Constructor cons = cl.getConstructor(constructorParams);
  608               return cons.newInstance(new Object[] { h });
  609           } catch (NoSuchMethodException e) {
  610               throw new InternalError(e.toString());
  611           } catch (IllegalAccessException e) {
  612               throw new InternalError(e.toString());
  613           } catch (InstantiationException e) {
  614               throw new InternalError(e.toString());
  615           } catch (InvocationTargetException e) {
  616               throw new InternalError(e.toString());
  617           }
  618       }
  619   
  620       /**
  621        * Returns true if and only if the specified class was dynamically
  622        * generated to be a proxy class using the {@code getProxyClass}
  623        * method or the {@code newProxyInstance} method.
  624        *
  625        * <p>The reliability of this method is important for the ability
  626        * to use it to make security decisions, so its implementation should
  627        * not just test if the class in question extends {@code Proxy}.
  628        *
  629        * @param   cl the class to test
  630        * @return  {@code true} if the class is a proxy class and
  631        *          {@code false} otherwise
  632        * @throws  NullPointerException if {@code cl} is {@code null}
  633        */
  634       public static boolean isProxyClass(Class<?> cl) {
  635           if (cl == null) {
  636               throw new NullPointerException();
  637           }
  638   
  639           return proxyClasses.containsKey(cl);
  640       }
  641   
  642       /**
  643        * Returns the invocation handler for the specified proxy instance.
  644        *
  645        * @param   proxy the proxy instance to return the invocation handler for
  646        * @return  the invocation handler for the proxy instance
  647        * @throws  IllegalArgumentException if the argument is not a
  648        *          proxy instance
  649        */
  650       public static InvocationHandler getInvocationHandler(Object proxy)
  651           throws IllegalArgumentException
  652       {
  653           /*
  654            * Verify that the object is actually a proxy instance.
  655            */
  656           if (!isProxyClass(proxy.getClass())) {
  657               throw new IllegalArgumentException("not a proxy instance");
  658           }
  659   
  660           Proxy p = (Proxy) proxy;
  661           return p.h;
  662       }
  663   
  664       private static native Class defineClass0(ClassLoader loader, String name,
  665                                                byte[] b, int off, int len);
  666   }

  Save This Page Home » openjdk-7 » java » lang » reflect » [javadoc | source]