package com.juanboris.springboot.backend.listaCompra.auth;

public class JwtConfig {
  public static final String LLAVA_SECRETA_STRING = "kY/56Gf&5tGAS4";

  public static final String RSA_PRIVADA_STRING = "-----BEGIN RSA PRIVATE KEY-----\r\n"
      + "MIIEpAIBAAKCAQEAzGrg6S6dyxGxnmnGB+0mDB07RGDGoKDjH98ZZDhfq0bpg4Ve\r\n"
      + "q0wXafHpD+3r7gFQB2FbuRWRzc41fsRoAchADppVeaCleqoRt6PueaXvN4GIL1Wa\r\n"
      + "RNKh6rxcj34qhK2NNs/mkwhrrzGx9lh9fw79Y1GXtr8cz9ivmzxzXq5ujzMldBd1\r\n"
      + "0QstbP718XVUtBjZuQyBPJrO77/yaDbgkG0o8jv2XP7f6FooT6USV5YvtUaVk+Uf\r\n"
      + "gw+f7dI4ZsXrA6BQGzgLhjK9y8fhdQ6F6uCtp0V3YLdRBE9fCfXm2ZDIzIumzRn0\r\n"
      + "QaUP/se965l7PWEiPpmRTuyFm/jNxM1G+MhfrQIDAQABAoIBAQCFC78tJeEfzXlc\r\n"
      + "kJ2KhShxVAlCHPbKj0ykbtGY8DQcR0AvEPwn45ONqhy1+HXJZ+NNmmlXkpleaSta\r\n"
      + "Qb0sA8jleD/PtOZfaxey/ah2VrDW4KDhpgaNasZmAIkB/+zuhALYPQJmimGEGoUo\r\n"
      + "wDmWHAyBuxZSpBq9kNvi3rp8Um21FzsSZa0QMjp3j3XU/jcO7td1ftu95WMxx7Zf\r\n"
      + "U8SsAit1b8Ncc9pBIffYWVM3uWMg7fDS6V4lin2Fwo4IMB7vt6TNg8urYLR9CooN\r\n"
      + "0fhlEisJo0Sai/tFUK9QfPBfPu8LRrbl4LBDQf6wW765m2bcjli+Q07ytt03peVf\r\n"
      + "rmnbGWbBAoGBAPlcnsLydc2mVxmtbRmrrsri++sN6zun7OW89giPVzdJa6DsP8pO\r\n"
      + "OL2uV8LBU2jqyTlcEmzGoKLlKYmKq2VQBZsmD4VHHzZhzUpd5IFJHlgZxWftizgh\r\n"
      + "B2yw3CPGTkYN3C8O9BLFyI1nvxeWaU5jASBz4SsK3lvPbGrrb6GBcfSxAoGBANHb\r\n"
      + "93sJdqXSihV7kKdR0KEwsAr2ucuqO9+MYtGuCr37ThZco7+onf4i8sv29KXma8C9\r\n"
      + "jOqwnlXBLEOnbSDMBYJKdL5/rPrPHkyk21LC0BJAYiDzqGqTChP2UvhyXCdsMb6L\r\n"
      + "7DdX+FJxVDEf5hssJWlDM/Q49yW7muXhI6Mj9Im9AoGAS5C5u7Hl6RADTRC1AxZ3\r\n"
      + "vZvTY4OwST+2FliQ5j8p2uMw7m8pVZEmuLRge/BB6oVbvTodi0EV5Mc1My0Gi4kY\r\n"
      + "ac+63FSVLNGueF7DpDoYK/KDU980VSoNKe7ehyjNB4Wjt878P8QX5mIOEoPOHab6\r\n"
      + "7G6xSW3dvEy7OhtmlLa5ZcECgYBJ6KdTPLT9l5WsFTWPx/+rFUOayOQMd2rYw092\r\n"
      + "O05rNFt/Aqdk85SZEWPjM5lb6DfgzKEZcpSA7SSSux9y4oe6KFDPWpXoSxOHuLPJ\r\n"
      + "hPTUyLoGkumohxqQhzsXZudPtwoZ6puJrkC1gFY/atWWkkimttVQWNOqBVV7SnxL\r\n"
      + "k3knHQKBgQClTFxipdg0+4yLnURpeYe86Y1XOajNyb6rGL1URTgpGMc7KDaebXb8\r\n"
      + "znKiNy4VcjocGjgWa6UljYfKcDwb7KCcmRvgSFvVXzNqr5lyHPrXxJDUGQHW4n/2\r\n"
      + "7xLrVqixxocQuPv5WGZuCOQ4erdAGxpzYA7+rvE+Wtc3npNTqmlUFA==\r\n"
      + "-----END RSA PRIVATE KEY-----";

  public static final String RSA_PUBLICA_STRING = "-----BEGIN PUBLIC KEY-----\r\n"
      + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzGrg6S6dyxGxnmnGB+0m\r\n"
      + "DB07RGDGoKDjH98ZZDhfq0bpg4Veq0wXafHpD+3r7gFQB2FbuRWRzc41fsRoAchA\r\n"
      + "DppVeaCleqoRt6PueaXvN4GIL1WaRNKh6rxcj34qhK2NNs/mkwhrrzGx9lh9fw79\r\n"
      + "Y1GXtr8cz9ivmzxzXq5ujzMldBd10QstbP718XVUtBjZuQyBPJrO77/yaDbgkG0o\r\n"
      + "8jv2XP7f6FooT6USV5YvtUaVk+Ufgw+f7dI4ZsXrA6BQGzgLhjK9y8fhdQ6F6uCt\r\n"
      + "p0V3YLdRBE9fCfXm2ZDIzIumzRn0QaUP/se965l7PWEiPpmRTuyFm/jNxM1G+Mhf\r\n" + "rQIDAQAB\r\n"
      + "-----END PUBLIC KEY-----";
}
