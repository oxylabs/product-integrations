package main

import (
	"encoding/base64"
	"fmt"
	"io"
	"net/http"
	"os"
)

const AuthUsername = "YOUR_USERNAME"
const AuthPassword = "YOUR_PASSWORD"

func main() {
	client := &http.Client{}

	request, err := http.NewRequest(
		"GET",
		"https://data.oxylabs.io/v1/queries/1234567890987654321/results",
		nil,
	)
	if err != nil {
		panic(err)
	}

	request.Header.Add("Content-Type", "application/json")
	request.Header.Add("Authorization", generateAuthHeader(AuthUsername, AuthPassword))

	response, err := client.Do(request)
	if err != nil {
		panic(err)
	}
	defer response.Body.Close()

	if response.StatusCode != http.StatusOK {
		fmt.Printf("Invalid status code received: %d\n", response.StatusCode)
		responseBytes, _ := io.ReadAll(response.Body)
		responseString := string(responseBytes)
		fmt.Println(responseString)

		os.Exit(1)
	}

	responseBytes, err := io.ReadAll(response.Body)
	if err != nil {
		panic(err)
	}

	responseString := string(responseBytes)
	fmt.Println(responseString)
}

func generateAuthHeader(username, password string) string {
	authCredentials := fmt.Sprintf("%s:%s", username, password)
	encodedCredentials := base64.StdEncoding.EncodeToString([]byte(authCredentials))

	return fmt.Sprintf("Basic %s", encodedCredentials)
}
