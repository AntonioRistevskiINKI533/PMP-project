# PMP-project
Семинарска по ПМП - Антонио Ристевски ИНКИ 533

Апликација за запишување на забелешки, Notes со наслов и текст.
При старт се појавува најава, имаа опции (горе во менито) да се избери јазик Македонски или Англиски.

Можна е најава со Email/Password (со претходна регистрација со избирање на регистрирај нов профил), Анонимна (гостин), Facebook и Google.
Профилите се поврзани со Firebase.

Постои и можност со опцијата saved users - снимени профили, да се најавиме со некој претходно најавен профил локалн на телефонот. 
Можно е и нивно бришење
-постигнато со локална база - Room library


![image](https://user-images.githubusercontent.com/62266696/173671411-dae79224-1f62-4952-8354-c69ca99ee525.png)

Откако ќе се најави корисикот се појавуваат неговите забелешки (како и неговиот мејл горе). Сите забелешки се чуваат на Firestore, и според UserID (секоја забелешки има податоци title, UID и text) се земаат и прикажваат за секој корисник посебно.

Корисникот можи со клик на кружното плус копче да додава нова забелешка, истата да ја сними или отфрли. После успешен внес се појавува таа во листата.

![image](https://user-images.githubusercontent.com/62266696/173672926-e10d7876-dc77-4432-891e-45541c14decf.png)

Со кликање на некоја од пораките од листата можен е и нејзин преглед, промена или бришење.

![image](https://user-images.githubusercontent.com/62266696/173676502-a3a7e030-41c3-438a-8966-2b8fd5f0e4a3.png)



