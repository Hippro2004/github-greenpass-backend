# คู่มือติดตั้ง Greenpass Backend

เอกสารนี้อธิบายการเตรียมเครื่อง ติดตั้ง dependency ตั้งค่าฐานข้อมูล และเริ่มใช้งาน Greenpass Backend สำหรับ Windows โดยอ้างอิงจากโครงสร้างและค่ากำหนดใน repository ปัจจุบัน

## 1. ภาพรวมระบบ

- Backend พัฒนาด้วย Java และ Spring Boot
- ใช้ MySQL เป็นฐานข้อมูล และ Spring Data JPA/Hibernate จัดการข้อมูล
- มี REST API ภายใต้ context path `/api/v1`
- เปิด WebSocket แบบ SockJS/STOMP ที่ `/api/v1/ws-greenpass`
- เก็บไฟล์ที่อัปโหลดลงในโฟลเดอร์ `uploads` ที่ตำแหน่งซึ่งใช้เริ่มโปรแกรม
- มี Maven Wrapper ใน repository จึงไม่จำเป็นต้องติดตั้ง Maven แยก
- มี dependency JavaScript สำหรับ STOMP/SockJS ใน `package.json` แต่ไม่มี npm script สำหรับ build หรือ start backend

## 2. สิ่งที่ต้องเตรียม

1. **Git** สำหรับดาวน์โหลด source code
2. **JDK 21** (ไม่ใช่เพียง JRE) สำหรับ compile และรันโปรแกรม
3. **MySQL Server** ที่เครื่องเข้าถึงได้ พร้อมสิทธิ์สร้างและปรับตารางใน database ที่จะใช้
4. **อินเทอร์เน็ต** ในการดาวน์โหลด Maven และ dependencies ครั้งแรกผ่าน Maven Wrapper
5. **PowerShell** หรือ Windows Terminal สำหรับรันคำสั่งด้านล่าง

ตรวจสอบ Java และ Git:

```powershell
java -version
git --version
```

ผล `java -version` ควรแสดงเวอร์ชัน 21

## 3. ดาวน์โหลดโปรเจกต์

```powershell
git clone https://github.com/Hippro2004/github-greenpass-backend.git
cd .\github-greenpass-backend
```

หากได้รับ source code เป็นไฟล์ ZIP ให้แตกไฟล์ แล้วเปิด PowerShell ที่โฟลเดอร์ซึ่งมี `pom.xml` และ `mvnw.cmd`

## 4. เตรียมฐานข้อมูล MySQL

สร้าง database และผู้ใช้สำหรับแอปพลิเคชัน ตัวอย่างคำสั่งต่อไปนี้ต้องรันใน MySQL client ด้วยบัญชีที่มีสิทธิ์จัดการผู้ใช้:

```sql
CREATE DATABASE greenpass_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

CREATE USER 'greenpass_user'@'localhost'
    IDENTIFIED BY 'เปลี่ยนเป็นรหัสผ่านที่ปลอดภัย';

GRANT ALL PRIVILEGES ON greenpass_db.* TO 'greenpass_user'@'localhost';
FLUSH PRIVILEGES;
```

สิทธิ์ข้างต้นจำกัดอยู่ที่ `greenpass_db` และรองรับการสร้าง/ปรับ schema อัตโนมัติในโหมดพัฒนา สำหรับ production ควรทบทวนสิทธิ์ของบัญชีฐานข้อมูลและกระบวนการจัดการ schema ก่อนใช้งานจริง

### ตั้งค่าการเชื่อมต่อ

โปรเจกต์อ่านค่าหลักจาก `src/main/resources/application.properties` โดยค่าฐานข้อมูลที่มีอยู่ในไฟล์ปัจจุบันอ้างถึง MySQL ภายนอก ไม่ใช่ MySQL ที่ติดตั้งในเครื่อง หากต้องการใช้ฐานข้อมูล local ให้ override ค่าด้วย environment variables ใน PowerShell ก่อน build หรือ start:

```powershell
$env:SPRING_DATASOURCE_URL = 'jdbc:mysql://localhost:3306/greenpass_db?useSSL=false&serverTimezone=UTC'
$env:SPRING_DATASOURCE_USERNAME = 'greenpass_user'
$env:SPRING_DATASOURCE_PASSWORD = 'เปลี่ยนเป็นรหัสผ่านที่ตั้งไว้'
```

Spring Boot จะนำค่าเหล่านี้ไปแทน `spring.datasource.url`, `spring.datasource.username` และ `spring.datasource.password` ใน properties โดยไม่ต้องแก้ไฟล์ที่ติดตามใน Git ตัวแปรจะอยู่ในหน้าต่าง PowerShell ปัจจุบันเท่านั้น ปิดหน้าต่างหรือใช้คำสั่งด้านล่างเพื่อล้างค่า:

```powershell
Remove-Item Env:SPRING_DATASOURCE_URL
Remove-Item Env:SPRING_DATASOURCE_USERNAME
Remove-Item Env:SPRING_DATASOURCE_PASSWORD
```

หากใช้ฐานข้อมูลภายนอก ให้ตั้ง `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` และ `SPRING_DATASOURCE_PASSWORD` เป็นค่าของฐานข้อมูลนั้น โดยตรวจสอบ network access และ SSL requirement จากผู้ให้บริการด้วย

> **ข้อควรระวัง:** อย่าเผยแพร่รหัสผ่านหรือ credential ของฐานข้อมูลใน Git, เอกสาร, screenshot หรือ log ค่าใน `application.properties` ที่ใช้อยู่เป็นค่าฐานข้อมูลภายนอก ควรใช้ environment variables/secret store ของเครื่องหรือระบบ deploy และเปลี่ยน credential หากเคยถูกเปิดเผย

## 5. Build และทดสอบ

จากโฟลเดอร์ root ของโปรเจกต์ ให้ตรวจสอบว่า MySQL ทำงานและ environment variables ถูกตั้งแล้ว จากนั้นรัน:

```powershell
.\mvnw.cmd clean test
```

สร้างไฟล์สำหรับรัน:

```powershell
.\mvnw.cmd clean package
```

Maven Wrapper จะดาวน์โหลด Maven ตามเวอร์ชันที่กำหนดใน `.mvn/wrapper/maven-wrapper.properties` โดยอัตโนมัติ การทดสอบที่โหลด Spring application context ต้องเชื่อมต่อฐานข้อมูลได้ด้วย

## 6. เริ่มโปรแกรม

### วิธีที่ 1: ใช้ Spring Boot Maven Plugin

```powershell
.\mvnw.cmd spring-boot:run
```

### วิธีที่ 2: รันไฟล์ JAR

หลังจาก build สำเร็จ:

```powershell
java -jar .\target\greenpass-0.0.1-SNAPSHOT.jar
```

ค่าเริ่มต้นในโปรเจกต์กำหนด port เป็น `8081` และ context path เป็น `/api/v1` ดังนั้น base URL คือ:

```text
http://localhost:8081/api/v1
```

หากต้องการให้รับการเชื่อมต่อเฉพาะเครื่อง local ให้ตั้งก่อนเริ่มโปรแกรม:

```powershell
$env:SERVER_ADDRESS = '127.0.0.1'
```

ค่าปัจจุบันของ `server.address` คือ `0.0.0.0` ซึ่งรับการเชื่อมต่อผ่านทุก network interface ควรจำกัดการเข้าถึงด้วย firewall หรือเปลี่ยนเป็น `127.0.0.1` หากไม่ต้องการให้เครื่องอื่นในเครือข่ายเชื่อมต่อ

## 7. ตรวจสอบหลังเริ่มระบบ

1. ดู log ในหน้าต่าง PowerShell ต้องไม่มีข้อความเชื่อมต่อฐานข้อมูลล้มเหลวหรือ application startup failure
2. ตรวจ health endpoint ที่:

   ```text
   http://localhost:8081/api/v1/actuator/health
   ```

   หาก health แสดง `DOWN` ให้ตรวจการเชื่อมต่อ MySQL และ credential ก่อน
3. ตรวจว่า REST API ตอบสนองที่ base path `/api/v1` โดยใช้ endpoint ที่ต้องการจาก controller ของระบบ
4. WebSocket/STOMP endpoint ใช้ `/api/v1/ws-greenpass` และตั้ง application destination prefix เป็น `/app` กับ broker prefix `/topic`
5. เมื่อต้องทดสอบการอัปโหลด ตรวจว่าแอปสร้าง/เขียนไฟล์ใน `uploads` ได้ ไฟล์จะถูกเสิร์ฟภายใต้ URL prefix `/uploads/`

## 8. การจัดการ schema และข้อมูลเริ่มต้น

- `spring.jpa.hibernate.ddl-auto=update` ทำให้ Hibernate พยายามสร้างหรือปรับตารางตาม entity เมื่อแอปเริ่มทำงาน
- ไม่มีไฟล์ SQL สำหรับสร้าง schema แยกใน repository ปัจจุบัน
- ตอนเริ่มระบบจะเพิ่มประเภท Report เริ่มต้น `ปกติ` และ `ร้ายแรง` หากยังไม่มีข้อมูลดังกล่าว
- เก็บ database ให้พร้อมก่อน start เพราะแอปต้องเชื่อมต่อฐานข้อมูลระหว่าง startup
- ก่อน deploy production ควรวางแผน migration/backup และพิจารณาใช้ schema migration ที่ควบคุมเวอร์ชัน แทนการให้แอปปรับ schema โดยอัตโนมัติ

## 9. ไฟล์อัปโหลดและพื้นที่จัดเก็บ

โฟลเดอร์ `uploads` เป็นพื้นที่จัดเก็บไฟล์บน filesystem ไม่ได้อยู่ใน database:

- อย่าลบโฟลเดอร์นี้ระหว่างอัปเดตหากต้องเก็บไฟล์ที่ผู้ใช้เคยอัปโหลด
- production/container ควร mount persistent volume หรือกำหนดพื้นที่จัดเก็บถาวร และสำรองข้อมูลแยกจาก database
- ขนาด multipart ที่กำหนดไว้ใน application properties คือสูงสุด 50 MB ต่อไฟล์/คำขอตามแต่ละ setting
- ตรวจสิทธิ์การเขียนของ account ที่ใช้รัน Java

## 10. การแก้ปัญหาเบื้องต้น

| อาการ | สิ่งที่ควรตรวจสอบ |
|---|---|
| `java` ไม่พบ หรือเวอร์ชันไม่ใช่ 21 | ติดตั้ง JDK 21 และตรวจ `JAVA_HOME`/`PATH` จากนั้นเปิด terminal ใหม่ |
| Maven Wrapper ดาวน์โหลดไม่ได้ | ตรวจอินเทอร์เน็ต, proxy และสิทธิ์เข้าถึง Maven Central |
| `Communications link failure` | ตรวจว่า MySQL ทำงาน, host/port ถูกต้อง และ firewall อนุญาต |
| `Access denied for user` | ตรวจชื่อผู้ใช้/รหัสผ่านและ grant ของ MySQL user สำหรับ database นั้น |
| Unknown database หรือสร้างตารางไม่ได้ | ตรวจว่ามี database แล้ว และ user มีสิทธิ์สร้าง/ปรับตาราง |
| Port 8081 ถูกใช้งานแล้ว | ปิดโปรแกรมที่ใช้ port หรือกำหนด `$env:SERVER_PORT = '8082'` แล้วลองใหม่ |
| อัปโหลดไฟล์ไม่ได้ | ตรวจสิทธิ์เขียนใน `uploads`, ขนาดคำขอ และพื้นที่ดิสก์ |
| Health endpoint แสดง `DOWN` | ตรวจข้อความ database health และการเชื่อมต่อฐานข้อมูล |

## 11. การหยุดโปรแกรม

กลับไปที่หน้าต่าง PowerShell ที่รันแอป แล้วกด `Ctrl+C`

