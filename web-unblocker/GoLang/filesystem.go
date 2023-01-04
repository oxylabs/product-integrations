package main

import (
	"bufio"
	"compress/gzip"
	"fmt"
	"io"
	"os"
	"strings"
)

const errorFilename = "failed_requests.txt"
const successFilename = "result_%d.html"

func readLines(path string) ([]string, error) {
	file, err := os.Open(path)
	if err != nil {
		return nil, err
	}
	defer file.Close()

	var lines []string
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}
	return lines, scanner.Err()
}

func writeErrorToStdout(format string, a ...interface{}) {
	fmt.Printf(format+"\n", a...)
}

func writeErrorToFile(format string, a ...interface{}) {
	message := fmt.Sprintf(format+"\n", a...)
	writeToFile(errorFilename, message)
}

func writeSuccessToFile(position int, responseStream io.Reader) {
	gzipReader, err := gzip.NewReader(responseStream)
	if err != nil {
		writeErrorToStdout("Failed to read gzip stream: " + err.Error())

		return
	}

	buf := new(strings.Builder)
	_, err = io.Copy(buf, gzipReader)
	if err != nil {
		writeErrorToStdout("Error when writing to file: " + err.Error())

		return
	}

	formattedFilename := fmt.Sprintf(successFilename, position)
	writeToFile(formattedFilename, buf.String())
}

func writeToFile(filename, text string) {
	f, err := os.OpenFile(filename, os.O_APPEND|os.O_WRONLY|os.O_CREATE, 0600)
	if err != nil {
		panic(err)
	}

	defer f.Close()

	if _, err = f.WriteString(text); err != nil {
		panic(err)
	}
}
