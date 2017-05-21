package com.xfdream.music.entity;

/**
 * Created by Modi on 2016/12/8.
 * 邮箱：1294432350@qq.com
 */

public class LrcStrInfo {

    /**
     * charset : utf8
     * content : WzAwOjAxLjgwXVNob250ZWxsZSAtIEltcG9zc2libGUNClswMDoxMC44MF1JIHJlbWVtYmVyIHllYXJzIGFnbw0KWzAwOjE0LjAwXVNvbWVvbmUgdG9sZCBtZSBJIHNob3VsZCB0YWtlDQpbMDA6MTYuMjBdQ2F1dGlvbiB3aGVuIGl0IGNvbWVzIHRvIGxvdmUNClswMDoxOC45MV1JIGRpZCwgSSBkaWQNClswMDoyMS4zNl1BbmQgeW91IHdlcmUgc3Ryb25nIGFuZCBJIHdhcyBub3QNClswMDoyNC4wMV1NeSBpbGx1c2lvbiwgbXkgbWlzdGFrZQ0KWzAwOjI2LjcxXUkgd2FzIGNhcmVsZXNzLCBJIGZvcmdvdA0KWzAwOjI5LjM0XUkgZGlkDQpbMDA6MzEuNjldQW5kIG5vdyB3aGVuIGFsbCBpcyBkb25lDQpbMDA6MzQuMDldVGhlcmUgaXMgbm90aGluZyB0byBzYXkNClswMDozNS40OV1Zb3UgaGF2ZSBnb25lIGFuZCBzbyBlZmZvcnRsZXNzbHkNClswMDozNy4wOV1Zb3UgaGF2ZSB3b24NClswMDozOS43MF1Zb3UgY2FuIGdvIGFoZWFkIHRlbGwgdGhlbQ0KWzAwOjQxLjY1XVRlbGwgdGhlbSBhbGwgSSBrbm93IG5vdw0KWzAwOjQzLjk1XVNob3V0IGl0IGZyb20gdGhlIHJvb2YgdG9wDQpbMDA6NDYuNjFdV3JpdGUgaXQgb24gdGhlIHNreSBsb3ZlDQpbMDA6NDkuMzFdQWxsIHdlIGhhZCBpcyBnb25lIG5vdw0KWzAwOjUyLjAxXVRlbGwgdGhlbSBJIHdhcyBoYXBweQ0KWzAwOjU0LjM3XUFuZCBteSBoZWFydCBpcyBicm9rZW4NClswMDo1Ny44Nl1BbGwgbXkgc2NhcnMgYXJlIG9wZW4NClswMDo1OS45MV1UZWxsIHRoZW0gd2hhdCBJIGhvcGVkIHdvdWxkIGJlDQpbMDE6MDMuMDZdSW1wb3NzaWJsZSwgaW1wb3NzaWJsZQ0KWzAxOjA4LjI3XUltcG9zc2libGUsIGltcG9zc2libGUNClswMToxNC43M11GYWxsaW5nIG91dCBvZiBsb3ZlIGlzIGhhcmQNClswMToxNy4zM11GYWxsaW5nIGZvciBiZXRyYXlhbCBpcyB3b3JzdA0KWzAxOjIwLjAwXUJyb2tlbiB0cnVzdCBhbmQgYnJva2VuIGhlYXJ0cw0KWzAxOjIyLjQ1XUkga25vdywgSSBrbm93DQpbMDE6MjUuNDBdVGhpbmtpbmcgYWxsIHlvdSBuZWVkIGlzIHRoZXJlDQpbMDE6MjcuOTZdQnVpbGRpbmcgZmFpdGggb24gbG92ZSBpcyB3b3JzdA0KWzAxOjMwLjcxXUVtcHR5IHByb21pc2VzIHdpbGwgd2Vhcg0KWzAxOjMzLjMxXUkga25vdyAoSSBrbm93KQ0KWzAxOjM2LjUxXUFuZCBub3cgd2hlbiBhbGwgaXMgZ29uZQ0KWzAxOjM4LjE2XVRoZXJlIGlzIG5vdGhpbmcgdG8gc2F5DQpbMDE6MzkuNzFdQW5kIGlmIHlvdSdyZSBkb25lIHdpdGggZW1iYXJyYXNzaW5nIG1lDQpbMDE6NDIuMDJdT24geW91ciBvd24geW91IGNhbiBnbyBhaGVhZCB0ZWxsIHRoZW0NClswMTo0NS4yNV1UZWxsIHRoZW0gYWxsIEkga25vdyBub3cNClswMTo0OC4wNV1TaG91dCBpdCBmcm9tIHRoZSByb29mIHRvcA0KWzAxOjUwLjcwXVdyaXRlIGl0IG9uIHRoZSBza3kgbG92ZQ0KWzAxOjUzLjQxXUFsbCB3ZSBoYWQgaXMgZ29uZSBub3cNClswMTo1Ni4xMV1UZWxsIHRoZW0gSSB3YXMgaGFwcHkNClswMTo1OC43MV1BbmQgbXkgaGVhcnQgaXMgYnJva2VuDQpbMDI6MDEuMzddQWxsIG15IHNjYXJzIGFyZSBvcGVuDQpbMDI6MDMuOTVdVGVsbCB0aGVtIHdoYXQgSSBob3BlZCB3b3VsZCBiZQ0KWzAyOjA3LjQwXUltcG9zc2libGUsIGltcG9zc2libGUNClswMjoxMi4zMV1JbXBvc3NpYmxlLCBpbXBvc3NpYmxlDQpbMDI6MTcuNzZdSW1wb3NzaWJsZSwgaW1wb3NzaWJsZQ0KWzAyOjIzLjA3XUltcG9zc2libGUsIGltcG9zc2libGUNClswMjoyOS4zN11Pb2ggaW1wb3NzaWJsZSAoeWVhaCB5ZWFoKQ0KWzAyOjQwLjA3XUkgcmVtZW1iZXIgeWVhcnMgYWdvDQpbMDI6NDIuNTNdU29tZW9uZSB0b2xkIG1lIEkgc2hvdWxkIHRha2UNClswMjo0NS40M11DYXV0aW9uIHdoZW4gaXQgY29tZXMgdG8gbG92ZQ0KWzAyOjQ4LjAxXUkgZGlkDQpbMDI6NDkuODFdVGVsbCB0aGVtIGFsbCBJIGtub3cgbm93DQpbMDI6NTIuMTFdU2hvdXQgaXQgZnJvbSB0aGUgcm9vZiB0b3ANClswMjo1NC43M11Xcml0ZSBpdCBvbiB0aGUgc2t5IGxvdmUNClswMjo1Ny4zNF1BbGwgd2UgaGFkIGlzIGdvbmUgbm93DQpbMDM6MDAuMDldVGVsbCB0aGVtIEkgd2FzIGhhcHB5KEkgd2FzIGhhcHB5KQ0KWzAzOjAzLjMwXUFuZCBteSBoZWFydCBpcyBicm9rZW4NClswMzowNS40NV1BbGwgbXkgc2NhcnMgYXJlIG9wZW4NClswMzowNy44MV1UZWxsIHRoZW0gd2hhdCBJIGhvcGVkIHdvdWxkIGJlDQpbMDM6MTEuMjFdSW1wb3NzaWJsZSwgaW1wb3NzaWJsZQ0KWzAzOjE2LjM2XUltcG9zc2libGUsIGltcG9zc2libGUNClswMzoyMS43MV1JbXBvc3NpYmxlLCBpbXBvc3NpYmxlDQpbMDM6MjcuMDFdSW1wb3NzaWJsZSwgaW1wb3NzaWJsZQ0KWzAzOjMyLjg5XUkgcmVtZW1iZXIgeWVhcnMgYWdvDQpbMDM6MzUuNzFdU29tZW9uZSB0b2xkIG1lIEkgc2hvdWxkIHRha2UNClswMzozOC43MV1DYXV0aW9uIHdoZW4gaXQgY29tZXMgdG8gbG92ZQ0KWzAzOjQxLjQ3XUkgZGlkDQpbMDM6NDMuMDddVEhFIEVORA0K
     * fmt : lrc
     * info : OK
     * status : 200
     */

    private String charset;
    private String content;
    private String fmt;
    private String info;
    private int status;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFmt() {
        return fmt;
    }

    public void setFmt(String fmt) {
        this.fmt = fmt;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
