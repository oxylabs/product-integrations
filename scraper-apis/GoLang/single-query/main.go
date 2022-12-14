package main

import (
	"bytes"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"os"
)

const AuthUsername = "YOUR_USERNAME"
const AuthPassword = "YOUR_PASSWORD"

type Parameters struct {
	Source      string `json:"source"`
	Query       string `json:"query"`
	GeoLocation string `json:"geo_location"`
	Parse       string `json:"parse"`
}

func main() {
	parameters := Parameters{
		Source:      "amazon_search",
		Query:       "kettle",
		GeoLocation: "10005",
		Parse:       "true",
	}

	encodedBody, err := json.Marshal(parameters)
	if err != nil {
		panic(err)
	}

	client := &http.Client{}

	request, err := http.NewRequest(
		"POST",
		"https://data.oxylabs.io/v1/queries",
		bytes.NewBuffer(encodedBody),
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

	if response.StatusCode != http.StatusAccepted {
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
