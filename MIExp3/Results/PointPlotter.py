#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Apr 24 09:14:54 2018

@author: colosu
"""

import matplotlib.pyplot as plt

Sizefile = "./size.txt"
MIfile = "./MI0.000001.txt"
Runfile = "./Run0.000001.txt"

SizeFile = open(Sizefile, "r")
MIFile = open(MIfile, "r")
RunFile = open(Runfile, "r")


Size = []
Size.append(0)
for line in SizeFile:
    Size.append(float(line))
MI = []
for line in MIFile:
    MI.append(float(line))
Run = []
for line in RunFile:
    Run.append(float(line))

SizeFile.close()
MIFile.close()
RunFile.close()
    
plt.plot(Size[1:len(Size)],MI[:len(MI)])
plt.plot(Size[1:len(Size)],Run[:len(Run)], '--')
plt.legend(['MI time','Run time'])
#plt.axis([0, 1000, 0, 0.07])
plt.xlabel('test suite size')
plt.ylabel('time')
my_xticks = ['0', '100', '200', '300', '400', '500', '600', '700', '800', '900', '1000']
plt.xticks(Size, my_xticks)
plt.show()