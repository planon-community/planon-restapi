# Planon JAX-RS

## Install

1. Upload to /tms/servicessdk
2. Reload TMS
3. Should see a message similar to the following in the catalina.out log

    ```console
    Running...
    INFO [12-01-2021 21:49:21, , thread=] Jax-Rs registry (v1) will be mounted upon the next request: JaxRSRegistry
    CATA 01/12/21 21:49:21 INFO [org.apache.catalina.core.ApplicationContext log] ROOT: Bundle: Dartmouth-REST (0.0.0) is started. 
    INFO [12-01-2021 21:49:22, f003xt4, thread=] Installing registry edu.dartmouth.planon.rest.osgi.Activator$JaxRSRegistry
    ```

4. Service can be accessed via [https://environment/services/sdk](https://environment/services/sdk)
5. In System settings --> Password security --> Security --> Access keys
    1. Generate key pair
6. Verify access keys is setup in layout for accounts
7. Setup access key for account

## Upgrade

```console
CATA 01/13/21 01:21:21 INFO [org.apache.catalina.core.ApplicationContext log] ROOT: Bundle: Dartmouth-REST (0.0.0) is removed. 
 INFO [13-01-2021 01:21:21, , thread=] Service registry (v1) will be unmounted upon the next request: JaxRSRegistry
 INFO [13-01-2021 01:21:22, , thread=] Jax-Rs registry (v1) will be mounted upon the next request: JaxRSRegistry
CATA 01/13/21 01:21:22 INFO [org.apache.catalina.core.ApplicationContext log] ROOT: Bundle: Dartmouth-REST (0.0.0) is started. 
 INFO [13-01-2021 01:21:32, f003xt4, thread=] Uninstalling registry edu.dartmouth.planon.rest.osgi.Activator$JaxRSRegistry@1ce89f10
 INFO [13-01-2021 01:21:32, f003xt4, thread=] Installing registry edu.dartmouth.planon.rest.osgi.Activator$JaxRSRegistry
```
