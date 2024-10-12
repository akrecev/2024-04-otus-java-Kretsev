# Задание 13

### Собственный IoC контейнер

Цель:
В процессе создания своего контекста понять как работает основная часть Spring framework.


Описание/Пошаговая инструкция выполнения домашнего задания:  

Обязательная часть:  
В классе AppComponentsContainerImpl реализовать обработку, полученной в конструкторе конфигурации, основываясь на разметке аннотациями из пакета appcontainer.
Так же необходимо реализовать методы getAppComponent
В итоге должно получиться работающее приложение. Менять можно только
класс AppComponentsContainerImpl

Дополнительное задание (можно не делать):  
Разделить AppConfig на несколько классов и распределить по ним создание компонентов. В AppComponentsContainerImpl добавить конструктор, который обрабатывает несколько классов-конфигураций  
Дополнительное задание (можно не делать):
В AppComponentsContainerImpl добавить конструктор, который принимает на вход имя пакета, и обрабатывает все имеющиеся там классы-конфигурации (см. зависимости в pom.xml)