#!/bin/bash

for plot in $(ls plots/*.eps)
do
    epstopdf $plot
done
