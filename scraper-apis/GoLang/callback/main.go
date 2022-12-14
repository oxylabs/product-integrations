package main

import (
	"encoding/base64"
	"errors"
	"fmt"
	"github.com/labstack/echo/v4"
	"io"
	"net/http"
)

const AuthUsername = "YOUR_USERNAME"
const AuthPassword = "YOUR_PASSWORD"

type CallbackRequest struct {
	JobId string `json:"job_id"`
}

func main() {
	echoServer := echo.New()
	echoServer.POST("/job_listener", func(context echo.Context) error {
		callbackRequest := CallbackRequest{}
		if err := context.Bind(&callbackRequest); err != nil {
			return err
		}

		if callbackRequest.JobId == "" {
			return errors.New("empty id received in POST data")
		}

		content, err := fetchQueryResultById(callbackRequest.JobId)
		if err != nil {
			return err
		}

		return context.String(http.StatusOK, content)
	})
	echoServer.Logger.Fatal(echoServer.Start(":8080"))

}

func fetchQueryResultById(id string) (string, error) {
	client := &http.Client{}

	formattedUrl := fmt.Sprintf(
		"https://data.oxylabs.io/v1/queries/%s/results",
		id,
	)
	request, err := http.NewRequest(
		"GET",
		formattedUrl,
		nil,
	)
	if err != nil {
		return "", err
	}

	request.Header.Add("Content-Type", "application/json")
	request.Header.Add("Authorization", generateAuthHeader(AuthUsername, AuthPassword))

	response, err := client.Do(request)
	if err != nil {
		return "", err
	}
	defer response.Body.Close()

	if response.StatusCode != http.StatusOK {
		responseBytes, _ := io.ReadAll(response.Body)
		responseString := string(responseBytes)
		fmt.Println(responseString)

		return "", errors.New(responseString)
	}

	responseBytes, err := io.ReadAll(response.Body)
	if err != nil {
		return "", err
	}

	return string(responseBytes), nil
}

func generateAuthHeader(username, password string) string {
	authCredentials := fmt.Sprintf("%s:%s", username, password)
	encodedCredentials := base64.StdEncoding.EncodeToString([]byte(authCredentials))

	return fmt.Sprintf("Basic %s", encodedCredentials)
}
