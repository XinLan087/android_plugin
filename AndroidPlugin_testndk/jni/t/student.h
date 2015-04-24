#include<string.h>
#include<iostream>

using namespace std;


class Student //类声明
{
public:
	char *display(); //公用成员函数原型声明
	string display2();
private:
	int num;
	char *name;
	char *sex;
};

