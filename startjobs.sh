#!/bin/bash


for i in {2..10}
do
    sage superGrundy.sage 1 $i > results/fixed$i &
done

for i in {2..10}
do
    sage superGrundy.sage 0 $i > results/any$i &
done
