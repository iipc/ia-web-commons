1.1.6
-----
* [WAT extractor: adding information in WAT's warcinfo](https://github.com/iipc/webarchive-commons/issues/47)
* [WAT extractor: missing WARC format version](https://github.com/iipc/webarchive-commons/issues/45)
* [WAT extractor: envelope structure does not conform to the WAT specification](https://github.com/iipc/webarchive-commons/issues/44)
* [WAT extractor: WARC-Date in all records should be the WAT record generation date](https://github.com/iipc/webarchive-commons/issues/43)
* [WAT extractor: WARC-Filename in the WAT warcinfo record should be the WAT filename itself](https://github.com/iipc/webarchive-commons/issues/42)

1.1.5
-----
* [Escape redirect URLs in RealCDXExtractorOutput](https://github.com/iipc/webarchive-commons/pull/36)
* [Tests fail on Windows](https://github.com/iipc/webarchive-commons/issues/2)
* [Test fails on Java 8](https://github.com/iipc/webarchive-commons/issues/31)
* [RecordingOutputStream can affect tcp packets sent in an undesirable way](https://github.com/iipc/webarchive-commons/issues/38)

1.1.4
-----
* [All dates should be independent of locale settings](https://github.com/iipc/webarchive-commons/pull/22)
* [Resolved fastutil conflict in dependencies](https://github.com/iipc/webarchive-commons/pull/24)

1.1.3
-----
* [Synchronised with IA fork](https://github.com/iipc/webarchive-commons/pull/18)
* [Updated to more recent Guava APIs](https://github.com/iipc/webarchive-commons/pull/17)
* [Fixed handling of uncompressed ARC files #13 and #14](https://github.com/iipc/webarchive-commons/pull/14)
* [Avoid pulling in the logback dependency IA#13](https://github.com/internetarchive/webarchive-commons/pull/13)

1.1.2
-----
* Fixed support for reading uncompressed WARCs, along with some unit testing. (https://github.com/iipc/webarchive-commons/pull/12)

1.1.1
-----
* Renamed from commons-webarchive to webarchive-commons (https://github.com/iipc/webarchive-commons/pull/8)
* Cope with malformed GZip extra fields as produced by wget 1.14 (https://github.com/iipc/webarchive-commons/pull/10)
* Switch to httpcomponents, and add IA deployment information. (https://github.com/iipc/webarchive-commons/pull/11)
