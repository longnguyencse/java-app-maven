Mong muốn : Ứng dụng có thể đọc flow call API từ file config hoặc từ databse H2 (in-memory)
Tại vì code như thế không quen thuộc nên code ứng dụng này sẽ chia làm các version sau:
1.0 Config những cái cơ bản, flow đc define trong code java, run by file bash.
###2.0 File config sẽ chứa flow call API from server 


Architecture ##Dragger 2+###  Retrofit + rxJava
Log4j (Logback) + Guava + Gson

+++++++++++++++++++ CONFIG FILE ++++++++++++++++++++++++++++++++++++++++++++++++++
1. Config so file trong file zip (original file)
    number-frame-annotated-file
2. Config so vong loop tao dummy data file zip upload len server by API
    limit-zip-file
3. Config file name cua file ZIP
    prefix-file-create
4. Config url server
    url-server
5. Folder container sample image for zip file
    folder-data-dummy
++++++++++++++++++ RUN +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

1. Uploading
   run : bin/uploading.sh
2. Downloading:
    run: bin/downloading.sh