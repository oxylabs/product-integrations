using System;
using Newtonsoft.Json;

public partial class CallbackDTO
{
    [JsonProperty("callback_url")]
    public Uri CallbackUrl { get; set; }

    [JsonProperty("client_id")]
    public long ClientId { get; set; }

    [JsonProperty("created_at")]
    public DateTimeOffset CreatedAt { get; set; }

    [JsonProperty("domain")]
    public string Domain { get; set; }

    [JsonProperty("id")]
    public string Id { get; set; }

    [JsonProperty("pages")]
    public long Pages { get; set; }

    [JsonProperty("render")]
    public object Render { get; set; }

    [JsonProperty("source")]
    public string Source { get; set; }

    [JsonProperty("start_page")]
    public long StartPage { get; set; }

    [JsonProperty("status")]
    public string Status { get; set; }

    [JsonProperty("subdomain")]
    public string Subdomain { get; set; }

    [JsonProperty("url")]
    public Uri Url { get; set; }

    [JsonProperty("updated_at")]
    public DateTimeOffset UpdatedAt { get; set; }

    [JsonProperty("user_agent_type")]
    public string UserAgentType { get; set; }

    [JsonProperty("_links")]
    public Link[] Links { get; set; }

    public override string ToString()
    {
        return JsonConvert.SerializeObject(this);
    }
}

public partial class Link
{
    [JsonProperty("rel")]
    public string Rel { get; set; }

    [JsonProperty("href")]
    public Uri Href { get; set; }

    [JsonProperty("method")]
    public string Method { get; set; }
}
