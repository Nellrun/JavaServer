import os

out = open("build.md", "w", encoding="UTF-8")

for path, dirs, filenames in os.walk(os.getcwd()):
    for name in filenames:
        if name.endswith('.md') and not (name == "build.md"):
            print(path + "\\" + name)

            with open(path + "\\" + name, "r", encoding="UTF-8") as f:
                out.write(f.read() + "\r\n<div class=\"page-break\"></div>\r\n")
out.close()
