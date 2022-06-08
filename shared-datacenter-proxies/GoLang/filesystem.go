package main

import (
	"bufio"
	"bytes"
	"fmt"
	"io"
	"os"
	"strings"
)

const errorFilename = "failed_requests.txt"
const successFilename = "result_%d.html"

func readProxyMap(path string) (map[string]string, error) {
	proxyList, err := readLines(path)
	if err != nil {
		return nil, err
	}

	proxyMap := make(map[string]string)
	for _, proxyUrl := range proxyList {
		matchResult := matchProxyUrl(proxyUrl)

		if country, ok := matchResult["country"]; ok {
			proxyMap[strings.ToUpper(country)] = proxyUrl
		} else {
			proxyMap[DefaultProxyIndexName] = proxyUrl
		}
	}

	return proxyMap, nil
}

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
	contents := streamToString(responseStream)
	formattedFilename := fmt.Sprintf(successFilename, position)
	writeToFile(formattedFilename, contents)
}

func streamToString(stream io.Reader) string {
	buf := new(bytes.Buffer)
	buf.ReadFrom(stream)
	return buf.String()
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
