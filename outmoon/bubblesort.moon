           % processing function definition: 
bubbleSort           sw bubbleSortlink(r0),r15
           % processing: n := size
           lw r1,size(r0)
           sw n(r0),r1
           % processing: t1 := 0
           lw r1,t1(r0)
           sw t1(r0),r1
           % processing: i := t1
           lw r1,t1(r0)
           sw i(r0),r1
           % processing: t2 := 0
           lw r1,t2(r0)
           sw t2(r0),r1
           % processing: j := t2
           lw r1,t2(r0)
           sw j(r0),r1
           % processing: t3 := 0
           lw r1,t3(r0)
           sw t3(r0),r1
           % processing: temp := t3
           lw r1,t3(r0)
           sw temp(r0),r1
gowhile1           % processing: t4 := 1
           lw r1,t4(r0)
           sw t4(r0),r1
           % processing: t5 := n - t4
           lw r2,n(r0)
           lw r3,t4(r0)
           sub r1,r2,r3
           sw t5(r0),r1
           % processing: t6 := i < t5
           lw r3,i(r0)
           lw r2,t5(r0)
           clt r1,r3,r2
           sw t6(r0),r1
           lw r1,t6(r0)
           bz r1 endwhile1
gowhile1           % processing: t7 := n - i
           lw r3,n(r0)
           lw r4,i(r0)
           sub r2,r3,r4
           sw t7(r0),r2
           % processing: t8 := 1
           lw r2,t8(r0)
           sw t8(r0),r2
           % processing: t9 := t7 - t8
           lw r4,t7(r0)
           lw r3,t8(r0)
           sub r2,r4,r3
           sw t9(r0),r2
           % processing: t10 := j < t9
           lw r3,j(r0)
           lw r4,t9(r0)
           clt r2,r3,r4
           sw t10(r0),r2
           lw r2,t10(r0)
           bz r2 endwhile1
           lw r3,j(r0)
           muli r5,r3,4.0
           lw r6,arr(r5)
           % processing: t11 := 1
           lw r3,t11(r0)
           sw t11(r0),r3
           % processing: t12 := j + t11
           lw r6,j(r0)
           lw r7,t11(r0)
           add r3,r6,r7
           sw t12(r0),r3
           lw r5,t12(r0)
           muli r3,r5,4.0
           lw r7,arr(r3)
           % processing: t11 := 1
           lw r3,t11(r0)
           sw t11(r0),r3
           % processing: t12 := j + t11
           lw r5,j(r0)
           lw r7,t11(r0)
           add r3,r5,r7
           sw t12(r0),r3
           % processing: t13 := arr > arr
           lw r3,arr(r0)
           lw r7,arr(r0)
           cgt r4,r3,r7
           sw t13(r0),r4
           lw r4,t13(r0)
           bz r4,else1
           lw r3,j(r0)
           muli r5,r3,4.0
           lw r6,arr(r5)
           % processing: temp := arr
           lw r7,arr(r0)
           sw temp(r0),r7
           lw r5,j(r0)
           muli r3,r5,4.0
           lw r6,arr(r3)
           % processing: t14 := 1
           lw r5,t14(r0)
           sw t14(r0),r5
           % processing: t15 := j + t14
           lw r6,j(r0)
           lw r8,t14(r0)
           add r5,r6,r8
           sw t15(r0),r5
           lw r3,t15(r0)
           muli r5,r3,4.0
           lw r8,arr(r5)
           % processing: t14 := 1
           lw r5,t14(r0)
           sw t14(r0),r5
           % processing: t15 := j + t14
           lw r3,j(r0)
           lw r8,t14(r0)
           add r5,r3,r8
           sw t15(r0),r5
           % processing: arr := arr
           lw r7,arr(r0)
           sw arr(r0),r7
           % processing: t16 := 1
           lw r8,t16(r0)
           sw t16(r0),r8
           % processing: t17 := j + t16
           lw r3,j(r0)
           lw r6,t16(r0)
           add r8,r3,r6
           sw t17(r0),r8
           lw r5,t17(r0)
           muli r8,r5,4.0
           lw r6,arr(r8)
           % processing: t16 := 1
           lw r8,t16(r0)
           sw t16(r0),r8
           % processing: t17 := j + t16
           lw r5,j(r0)
           lw r6,t16(r0)
           add r8,r5,r6
           sw t17(r0),r8
           % processing: arr := temp
           lw r7,temp(r0)
           sw arr(r0),r7
           j endif1else1endif1           % processing: t18 := 1
           lw r4,t18(r0)
           sw t18(r0),r4
           % processing: t19 := j + t18
           lw r7,j(r0)
           lw r8,t18(r0)
           add r4,r7,r8
           sw t19(r0),r4
           % processing: j := t19
           lw r4,t19(r0)
           sw j(r0),r4
           j gowhile1
enndwhile1
           % processing: t20 := 1
           lw r4,t20(r0)
           sw t20(r0),r4
           % processing: t21 := i + t20
           lw r8,i(r0)
           lw r7,t20(r0)
           add r4,r8,r7
           sw t21(r0),r4
           % processing: i := t21
           lw r4,t21(r0)
           sw i(r0),r4
           j gowhile1
enndwhile1
           lw r15,Func Deflink(r0)
           jr r15
           % processing function definition: 
printArray           sw printArraylink(r0),r15
           % processing: n := size
           lw r4,size(r0)
           sw n(r0),r4
           % processing: t22 := 0
           lw r4,t22(r0)
           sw t22(r0),r4
           % processing: i := t22
           lw r4,t22(r0)
           sw i(r0),r4
gowhile1           % processing: t23 := i < n
           lw r7,i(r0)
           lw r8,n(r0)
           clt r4,r7,r8
           sw t23(r0),r4
           lw r4,t23(r0)
           bz r4 endwhile1
           lw r7,i(r0)
           muli r6,r7,4.0
           lw r5,arr(r6)
           % processing: put(arr)
           lw r8,arr(r0)
           % put value on stack
           sw -8(r14),r8
           % link buffer to stack
           addi r8,r0, buf
           sw -12(r14),r8
           % convert int to string for output
           jl r15, intstr
           sw -8(r14),r13
           % output to console
           jl r15, putstr
           % processing: t24 := 1
           lw r8,t24(r0)
           sw t24(r0),r8
           % processing: t25 := i + t24
           lw r6,i(r0)
           lw r7,t24(r0)
           add r8,r6,r7
           sw t25(r0),r8
           % processing: i := t25
           lw r8,t25(r0)
           sw i(r0),r8
           j gowhile1
enndwhile1
           lw r15,Func Deflink(r0)
           jr r15
           entry
           addi r14,r0,topaddr
           % processing: t26 := 0
           lw r6,t26(r0)
           sw t26(r0),r6
           lw r7,t26(r0)
           muli r6,r7,4.0
           lw r5,arr(r6)
           % processing: t26 := 0
           lw r6,t26(r0)
           sw t26(r0),r6
           % processing: t27 := 64
           lw r8,t27(r0)
           sw t27(r0),r8
           % processing: arr := t27
           lw r8,t27(r0)
           sw arr(r0),r8
           % processing: t28 := 1
           lw r7,t28(r0)
           sw t28(r0),r7
           lw r6,t28(r0)
           muli r7,r6,4.0
           lw r5,arr(r7)
           % processing: t28 := 1
           lw r7,t28(r0)
           sw t28(r0),r7
           % processing: t29 := 34
           lw r8,t29(r0)
           sw t29(r0),r8
           % processing: arr := t29
           lw r8,t29(r0)
           sw arr(r0),r8
           % processing: t30 := 2
           lw r6,t30(r0)
           sw t30(r0),r6
           lw r7,t30(r0)
           muli r6,r7,4.0
           lw r5,arr(r6)
           % processing: t30 := 2
           lw r6,t30(r0)
           sw t30(r0),r6
           % processing: t31 := 25
           lw r8,t31(r0)
           sw t31(r0),r8
           % processing: arr := t31
           lw r8,t31(r0)
           sw arr(r0),r8
           % processing: t32 := 3
           lw r7,t32(r0)
           sw t32(r0),r7
           lw r6,t32(r0)
           muli r7,r6,4.0
           lw r5,arr(r7)
           % processing: t32 := 3
           lw r7,t32(r0)
           sw t32(r0),r7
           % processing: t33 := 12
           lw r8,t33(r0)
           sw t33(r0),r8
           % processing: arr := t33
           lw r8,t33(r0)
           sw arr(r0),r8
           % processing: t34 := 4
           lw r6,t34(r0)
           sw t34(r0),r6
           lw r7,t34(r0)
           muli r6,r7,4.0
           lw r5,arr(r6)
           % processing: t34 := 4
           lw r6,t34(r0)
           sw t34(r0),r6
           % processing: t35 := 22
           lw r8,t35(r0)
           sw t35(r0),r8
           % processing: arr := t35
           lw r8,t35(r0)
           sw arr(r0),r8
           % processing: t36 := 5
           lw r7,t36(r0)
           sw t36(r0),r7
           lw r6,t36(r0)
           muli r7,r6,4.0
           lw r5,arr(r7)
           % processing: t36 := 5
           lw r7,t36(r0)
           sw t36(r0),r7
           % processing: t37 := 11
           lw r8,t37(r0)
           sw t37(r0),r8
           % processing: arr := t37
           lw r8,t37(r0)
           sw arr(r0),r8
           % processing: t38 := 6
           lw r6,t38(r0)
           sw t38(r0),r6
           lw r7,t38(r0)
           muli r6,r7,4.0
           lw r5,arr(r6)
           % processing: t38 := 6
           lw r6,t38(r0)
           sw t38(r0),r6
           % processing: t39 := 90
           lw r8,t39(r0)
           sw t39(r0),r8
           % processing: arr := t39
           lw r8,t39(r0)
           sw arr(r0),r8
           % processing: t40 := 7
           lw r8,t40(r0)
           sw t40(r0),r8
           % processing: function call to printarray 
           lw r8,arr(r0)
           sw arr(r0),r8
           lw r8,t40(r0)
           sw size(r0),r8
           jl r15,Fcall
           lw r8,Fcallreturn(r0)
           sw t41(r0),r8
           % processing: t42 := 7
           lw r8,t42(r0)
           sw t42(r0),r8
           % processing: function call to bubbleSort 
           lw r8,arr(r0)
           sw arr(r0),r8
           lw r8,t42(r0)
           sw size(r0),r8
           jl r15,Fcall
           lw r8,Fcallreturn(r0)
           sw t43(r0),r8
           % processing: t44 := 7
           lw r8,t44(r0)
           sw t44(r0),r8
           % processing: function call to printarray 
           lw r8,arr(r0)
           sw arr(r0),r8
           lw r8,t44(r0)
           sw size(r0),r8
           jl r15,Fcall
           lw r8,Fcallreturn(r0)
           sw t45(r0),r8
           hlt
bubbleSortlinkres 4
bubbleSortreturnres 0
           % space for variable n
n          res 4
           % space for variable i
i          res 4
           % space for variable j
j          res 4
           % space for variable temp
temp       res 4
           % space for t1
t1         res 4
           % space for t2
t2         res 4
           % space for t3
t3         res 4
           % space for t4
t4         res 4
           % space for n + t4
t5         res 4
           % space for i + t5
t6         res 4
           % space for n + i
t7         res 4
           % space for t8
t8         res 4
           % space for t7 + t8
t9         res 4
           % space for j + t9
t10        res 4
           % space for t11
t11        res 4
           % space for j + t11
t12        res 4
           % space for t11
t11        res 4
           % space for j + t11
t12        res 4
           % space for arr + arr
t13        res 0
           % space for t14
t14        res 4
           % space for j + t14
t15        res 4
           % space for t14
t14        res 4
           % space for j + t14
t15        res 4
           % space for t16
t16        res 4
           % space for j + t16
t17        res 4
           % space for t16
t16        res 4
           % space for j + t16
t17        res 4
           % space for t18
t18        res 4
           % space for j + t18
t19        res 4
           % space for t20
t20        res 4
           % space for i + t20
t21        res 4
printArraylinkres 4
printArrayreturnres 0
           % space for variable n
n          res 4
           % space for variable i
i          res 4
           % space for t22
t22        res 4
           % space for i + n
t23        res 4
           % space for t24
t24        res 4
           % space for i + t24
t25        res 4
           % space for variable arr
arr        res 28
           % space for t26
t26        res 4
           % space for t26
t26        res 4
           % space for t27
t27        res 4
           % space for t28
t28        res 4
           % space for t28
t28        res 4
           % space for t29
t29        res 4
           % space for t30
t30        res 4
           % space for t30
t30        res 4
           % space for t31
t31        res 4
           % space for t32
t32        res 4
           % space for t32
t32        res 4
           % space for t33
t33        res 4
           % space for t34
t34        res 4
           % space for t34
t34        res 4
           % space for t35
t35        res 4
           % space for t36
t36        res 4
           % space for t36
t36        res 4
           % space for t37
t37        res 4
           % space for t38
t38        res 4
           % space for t38
t38        res 4
           % space for t39
t39        res 4
           % space for t40
t40        res 4
           % space for function call expression factor
t41        res 4
           % space for t42
t42        res 4
           % space for function call expression factor
t43        res 4
           % space for t44
t44        res 4
           % space for function call expression factor
t45        res 4
           % buffer space used for console output
buf        res 20
