package main

import (
  "fmt"
  "net/http"
  "os"
  "os/exec"
  "io"
  "io/ioutil"
  "html"
  "encoding/json"
)

func uploadHandler(w http.ResponseWriter, r *http.Request) {
  fmt.Println("Connection opened!")

  file, header, err := r.FormFile("file") // the FormFile function takes in the POST input id file
  _ = header

  if err != nil {
    fmt.Fprintln(w, err)
    return
  }

  defer file.Close()

  out, err := os.Create("/tmp/uploadedfile")
  if err != nil {
    fmt.Fprintf(w, "Unable to create the file for writing. Check your write access privilege")
    return
  }

  defer out.Close()

  // write the content from POST to the file
  _, err = io.Copy(out, file)
  if err != nil {
    fmt.Fprintln(w, err)
  }

  fmt.Println("Got file, starting OCR!")

  cmd := exec.Command("tesseract", "-l", "eng", "/tmp/uploadedfile", "/tmp/out", "hocr")
  err = cmd.Run()
  if err != nil {
    fmt.Println(err)
  }

  fmt.Println("OCR done, answering!")

  in, err := os.Open("/tmp/out.hocr")

  if err != nil {
    fmt.Fprintln(w, err)
  }

  defer in.Close()

  _, err = io.Copy(w, in)

  fmt.Println("All done!")
}


type AResponse struct {
  Results [] struct {
    Answer string
    Question string
  }
}

func answer(questions []string) {
  resp, err := http.Get("http://gravity.answers.com/question/search?keyword=" + html.EscapeString(questions[0]))

  if err != nil {
    return
  }

  defer resp.Body.Close()
  contents, err := ioutil.ReadAll(resp.Body)

  if err != nil {
    return
  }

  var r AResponse

  err = json.Unmarshal(contents, &r)

  fmt.Println(r.Results[0].Answer)
}

func main() {
  answer([]string{"Who is John Snow?"})

  http.HandleFunc("/", uploadHandler)
  http.ListenAndServe(":8080", nil)
}

