           entry
           addi r14,r0,topaddr
           % processing: t1 := 1
           lw r1,t1(r0)
           sw t1(r0),r1
           % processing: t2 := 2
           lw r1,t2(r0)
           sw t2(r0),r1
           % processing: t3 := 3
           lw r1,t3(r0)
           sw t3(r0),r1
           % processing: t4 := t2 * t3
           lw r2,t2(r0)
           lw r3,t3(r0)
           mul r1,r2,r3
           sw t4(r0),r1
           % processing: t5 := t1 + t4
           lw r3,t1(r0)
           lw r2,t4(r0)
           add r1,r3,r2
           sw t5(r0),r1
           % processing: y := t5
           lw r1,t5(r0)
           sw y(r0),r1
           % processing: t6 := 10
           lw r1,t6(r0)
           sw t6(r0),r1
           % processing: t7 := y + t6
           lw r2,y(r0)
           lw r3,t6(r0)
           add r1,r2,r3
           sw t7(r0),r1
           % processing: t8 := x > t7
           lw r3,x(r0)
           lw r2,t7(r0)
           cgt r1,r3,r2
           sw t8(r0),r1
           lw r1,t8(r0)
           bz r1,else1
           % processing: t9 := 10
           lw r2,t9(r0)
           sw t9(r0),r2
           % processing: t10 := x + t9
           lw r3,x(r0)
           lw r4,t9(r0)
           add r2,r3,r4
           sw t10(r0),r2
           % processing: put(t9)
           lw r2,t9(r0)
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
           j endif1else1           % processing: t11 := 1
           lw r2,t11(r0)
           sw t11(r0),r2
           % processing: t12 := x + t11
           lw r4,x(r0)
           lw r3,t11(r0)
           add r2,r4,r3
           sw t12(r0),r2
           % processing: put(t11)
           lw r2,t11(r0)
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
endif1           % processing: t13 := 0
           lw r1,t13(r0)
           sw t13(r0),r1
           % processing: z := t13
           lw r1,t13(r0)
           sw z(r0),r1
gowhile1           % processing: t14 := 10
           lw r1,t14(r0)
           sw t14(r0),r1
           % processing: t15 := z <= t14
           lw r2,z(r0)
           lw r3,t14(r0)
           cle r1,r2,r3
           sw t15(r0),r1
           lw r1,t15(r0)
           bz r1 endwhile1
           % processing: put(z)
           lw r3,z(r0)
           % put value on stack
           sw -8(r14),r3
           % link buffer to stack
           addi r3,r0, buf
           sw -12(r14),r3
           % convert int to string for output
           jl r15, intstr
           sw -8(r14),r13
           % output to console
           jl r15, putstr
           j gowhile1
enndwhile1
           hlt
           % space for variable x
x          res 4
           % space for variable y
y          res 4
           % space for variable z
z          res 4
           % space for t1
t1         res 4
           % space for t2
t2         res 4
           % space for t3
t3         res 4
           % space for t2 + t3
t4         res 4
           % space for t1 + t4
t5         res 4
           % space for t6
t6         res 4
           % space for y + t6
t7         res 4
           % space for x + t7
t8         res 4
           % space for t9
t9         res 4
           % space for x + t9
t10        res 4
           % space for t11
t11        res 4
           % space for x + t11
t12        res 4
           % space for t13
t13        res 4
           % space for t14
t14        res 4
           % space for z + t14
t15        res 4
           % buffer space used for console output
buf        res 20
