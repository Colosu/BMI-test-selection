################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../lib/gzipcomplete.cpp \
../lib/mainpage.cpp \
../lib/raw.cpp \
../lib/zlibmisc.cpp \
../lib/zlibtop.cpp 

OBJS += \
./lib/gzipcomplete.o \
./lib/mainpage.o \
./lib/raw.o \
./lib/zlibmisc.o \
./lib/zlibtop.o 

CPP_DEPS += \
./lib/gzipcomplete.d \
./lib/mainpage.d \
./lib/raw.d \
./lib/zlibmisc.d \
./lib/zlibtop.d 


# Each subdirectory must supply rules for building sources it contributes
lib/%.o: ../lib/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -std=c++0x -fPIC -I"/home/Colosu/workspace/BMIvsTSDm/lib" -include/usr/include/fst/fstlib.h -include"/home/Colosu/workspace/BMIvsTSDm/lib/zlc/zlibcomplete.hpp" -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


