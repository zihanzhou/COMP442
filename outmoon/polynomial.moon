           % processing function definition: 
evaluate             sw evaluatelink(r0),r15
           % processing: t1 := 0
           lw r2,t1(r0)
           sw t1(r0),r2
           % processing: return(t1)
           lw r1,t1(r0)
           sw POLYNOMIAL::evaluatereturn(r0),r1
           lw r15,Func Deflink(r0)
           jr r15
           % processing function definition: 
evaluate             sw evaluatelink(r0),r15
           % processing: t2 := 0.0
           lw r1,t2(r0)
           sw t2(r0),r1
           % processing: result := t2
           lw r1,t2(r0)
           sw result(r0),r1
           % processing: t3 := a * x
           lw r2,a(r0)
           lw r3,x(r0)
           mul r1,r2,r3
           sw t3(r0),r1
           % processing: t4 := t3 + b
           lw r3,t3(r0)
           lw r2,b(r0)
           add r1,r3,r2
           sw t4(r0),r1
           % processing: result := t4
           lw r1,t4(r0)
           sw result(r0),r1
           % processing: return(result)
           lw r1,result(r0)
           sw LINEAR::evaluatereturn(r0),r1
           lw r15,Func Deflink(r0)
           jr r15
           % processing function definition: 
evaluate             sw evaluatelink(r0),r15
           % processing: result := a
           lw r1,a(r0)
           sw result(r0),r1
           % processing: t5 := result * x
           lw r2,result(r0)
           lw r3,x(r0)
           mul r1,r2,r3
           sw t5(r0),r1
           % processing: t6 := t5 + b
           lw r3,t5(r0)
           lw r2,b(r0)
           add r1,r3,r2
           sw t6(r0),r1
           % processing: result := t6
           lw r1,t6(r0)
           sw result(r0),r1
           % processing: t7 := result * x
           lw r2,result(r0)
           lw r3,x(r0)
           mul r1,r2,r3
           sw t7(r0),r1
           % processing: t8 := t7 + c
           lw r3,t7(r0)
           lw r2,c(r0)
           add r1,r3,r2
           sw t8(r0),r1
           % processing: result := t8
           lw r1,t8(r0)
           sw result(r0),r1
           % processing: return(result)
           lw r1,result(r0)
           sw QUADRATIC::evaluatereturn(r0),r1
           lw r15,Func Deflink(r0)
           jr r15
           % processing function definition: 
build                sw buildlink(r0),r15
           % processing:  := A
           lw r1,A(r0)
           sw (r0),r1
           % processing: put(a)
           lw r1,a(r0)
           % put value on stack
           sw -8(r14),r1
           % link buffer to stack
           addi r1,r0, buf
           sw -12(r14),r1
           % convert int to string for output
           jl r15, intstr
           sw -8(r14),r13
           % output to console
           jl r15, putstr
           % processing: return(new_function)
           lw r1,new_function(r0)
           sw LINEAR::buildreturn(r0),r1
           lw r15,Func Deflink(r0)
           jr r15
           % processing function definition: 
build                sw buildlink(r0),r15
           % processing:  := A
           lw r1,A(r0)
           sw (r0),r1
           % processing:  := B
           lw r1,B(r0)
           sw (r0),r1
           % processing:  := C
           lw r1,C(r0)
           sw (r0),r1
           % processing: return(new_function)
           lw r1,new_function(r0)
           sw QUADRATIC::buildreturn(r0),r1
           lw r15,Func Deflink(r0)
           jr r15
           entry
           addi r14,r0,topaddr
           % processing: t9 := 0
           lw r1,t9(r0)
           sw t9(r0),r1
           % processing: i := t9
           lw r1,t9(r0)
           sw i(r0),r1
           % processing: t10 := 1
           lw r3,t10(r0)
           sw t10(r0),r3
           lw r2,t10(r0)
           muli r3,r2,8.0
           lw r4,arr(r3)
           % processing: t10 := 1
           lw r3,t10(r0)
           sw t10(r0),r3
           % processing: t11 := 0.0
           lw r1,t11(r0)
           sw t11(r0),r1
           % processing: arr := t11
           lw r1,t11(r0)
           sw arr(r0),r1
           % processing: t12 := 1
           lw r2,t12(r0)
           sw t12(r0),r2
           lw r3,t12(r0)
           muli r2,r3,8.0
           lw r4,d(r2)
           % processing: t12 := 1
           lw r2,t12(r0)
           sw t12(r0),r2
           % processing: put(d)
           lw r1,d(r0)
           % put value on stack
           sw -8(r14),r1
           % link buffer to stack
           addi r1,r0, buf
           sw -12(r14),r1
           % convert int to string for output
           jl r15, intstr
           sw -8(r14),r13
           % output to console
           jl r15, putstr
           % processing: t13 := f2 + f2
           lw r2,f2(r0)
           lw r3,f2(r0)
           add r1,r2,r3
           sw t13(r0),r1
           % processing: f2 := t13
           lw r1,t13(r0)
           sw f2(r0),r1
           % processing: t14 := 2.0
           lw r1,t14(r0)
           sw t14(r0),r1
           % processing: t15 := 3.5
           lw r1,t15(r0)
           sw t15(r0),r1
           % processing: function call to build 
           lw r1,t14(r0)
           sw A(r0),r1
           lw r1,t15(r0)
           sw B(r0),r1
           jl r15,Fcall
           lw r1,Fcallreturn(r0)
           sw t16(r0),r1
           % processing: f1 := 
           lw r1,(r0)
           sw f1(r0),r1
           % processing: t17 := 2.0
           lw r1,t17(r0)
           sw t17(r0),r1
           % processing: t18 := 1.0
           lw r1,t18(r0)
           sw t18(r0),r1
           % processing: t19 := 0.0
           lw r1,t19(r0)
           sw t19(r0),r1
           % processing: function call to build 
           lw r1,(r0)
           sw A(r0),r1
           lw r1,t18(r0)
           sw B(r0),r1
           lw r1,t19(r0)
           sw new_function(r0),r1
           jl r15,Fcall
           lw r1,Fcallreturn(r0)
           sw t20(r0),r1
           % processing: f2 := 
           lw r1,(r0)
           sw f2(r0),r1
           % processing: t21 := 1.0
           lw r1,t21(r0)
           sw t21(r0),r1
           % processing: counter := t21
           lw r1,t21(r0)
           sw counter(r0),r1
gowhile1           % processing: t22 := 10.0
           lw r1,t22(r0)
           sw t22(r0),r1
           % processing: t23 := counter <= t22
           lw r3,counter(r0)
           lw r2,t22(r0)
           cle r1,r3,r2
           sw t23(r0),r1
           lw r1,t23(r0)
           bz r1 endwhile1
           % processing: put(counter)
           lw r2,counter(r0)
           % put value on stack
           sw -8(r14),r2
           % link buffer to stack
           addi r2,r0, buf
           sw -12(r14),r2
           % convert int to string for output
           jl r15, intstr
           sw -8(r14),r13
           % output to console
           jl r15, putstr
           % processing: function call to evaluate 
           lw r2,counter(r0)
           sw x(r0),r2
           jl r15,Fcall
           lw r2,Fcallreturn(r0)
           sw t24(r0),r2
           % processing: put(evaluate)
           lw r2,evaluate(r0)
           % put value on stack
           sw -8(r14),r2
           % link buffer to stack
           addi r2,r0, buf
           sw -12(r14),r2
           % convert int to string for output
           jl r15, intstr
           sw -8(r14),r13
           % output to console
           jl r15, putstr
           % processing: function call to evaluate 
           lw r2,counter(r0)
           sw x(r0),r2
           jl r15,Fcall
           lw r2,Fcallreturn(r0)
           sw t25(r0),r2
           % processing: put(evaluate)
           lw r2,evaluate(r0)
           % put value on stack
           sw -8(r14),r2
           % link buffer to stack
           addi r2,r0, buf
           sw -12(r14),r2
           % convert int to string for output
           jl r15, intstr
           sw -8(r14),r13
           % output to console
           jl r15, putstr
           j gowhile1
enndwhile1
           hlt
           % space for variable a
a          res 8
           % space for variable b
b          res 8
           % space for variable a
a          res 8
           % space for variable b
b          res 8
           % space for variable c
c          res 8
           % space for variable d
d          res 80
evaluatelinkres 4
evaluatereturnres 8
           % space for t1
t1         res 4
evaluatelinkres 4
evaluatereturnres 8
           % space for variable result
result     res 8
           % space for t2
t2         res 8
           % space for a + x
t3         res 8
           % space for t3 + b
t4         res 8
evaluatelinkres 4
evaluatereturnres 8
           % space for variable result
result     res 8
           % space for result + x
t5         res 8
           % space for t5 + b
t6         res 8
           % space for result + x
t7         res 8
           % space for t7 + c
t8         res 8
buildlink  res 4
buildreturnres 20
           % space for variable new_function
new_function res 20
buildlink  res 4
buildreturnres 108
           % space for variable new_function
new_function res 108
           % space for variable f1
f1         res 20
           % space for variable f2
f2         res 108
           % space for variable counter
counter    res 8
           % space for variable arr
arr        res 56
           % space for variable i
i          res 4
           % space for t9
t9         res 4
           % space for t10
t10        res 4
           % space for t10
t10        res 4
           % space for t11
t11        res 8
           % space for t12
t12        res 4
           % space for t12
t12        res 4
           % space for f2 + f2
t13        res 108
           % space for t14
t14        res 8
           % space for t15
t15        res 8
           % space for function call expression factor
t16        res 4
           % space for t17
t17        res 8
           % space for t18
t18        res 8
           % space for t19
t19        res 8
           % space for function call expression factor
t20        res 4
           % space for t21
t21        res 8
           % space for t22
t22        res 8
           % space for counter + t22
t23        res 8
           % space for function call expression factor
t24        res 4
           % space for function call expression factor
t25        res 4
           % buffer space used for console output
buf        res 20
