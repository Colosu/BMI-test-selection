################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/Checkups.cpp \
../src/Graph.cpp \
../src/IOHandler.cpp \
../src/IOpair.cpp \
../src/Mutations.cpp \
../src/Operations.cpp \
../src/View.cpp 

OBJS += \
./src/Checkups.o \
./src/Graph.o \
./src/IOHandler.o \
./src/IOpair.o \
./src/Mutations.o \
./src/Operations.o \
./src/View.o 

CPP_DEPS += \
./src/Checkups.d \
./src/Graph.d \
./src/IOHandler.d \
./src/IOpair.d \
./src/Mutations.d \
./src/Operations.d \
./src/View.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -std=c++0x -fPIC -I/usr/include -include/usr/include/fst/fstlib.h -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


