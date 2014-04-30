#!/bin/bash

for result in $(ls results)
do

    echo '
    #set terminal png size 1400,900
    #set output "plots/'$result'.png"

    set terminal postscript eps enhanced color
    set output "plots/'$result'.eps"

    set xlabel "Pile size"
    set ylabel "SG-value"

    set nokey

    plot "results/'$result'" every ::1 with points pointtype 7 pointsize 0.5

    pause -1' | gnuplot -persist

done

