"use strict";
// Removes the noise from the JUnit stack trace

let rules = [{
  pattern: /at org.junit/,
  reject: true
},{
  pattern: /at junit.framework/,
  reject: true
},{
  pattern: /at sun.reflect/,
  reject: true
},{
  pattern: /at java.lang.reflect/,
  reject: true
},{
  pattern: /FAILURES!!!/,
  reject: true,
  after: () => { process.exit(1) }
}]

let after = [];

let doctor = (chunk) => {
  var trace = chunk.split('\n')
  let newTrace = [];
  var match = null;
  trace.forEach(line => {
    let reject = false
    rules.forEach(rule => {
      let match = line.match(rule.pattern)
      if (match) {
        if (rule.reject) {
          reject = true;
          //console.log('reject', line, rule);
        }
        if (rule.after) {
          after.push(rule.after);
        }
        return false
      }
    })
    if (!reject) {
      newTrace.push(line);
    }
  })
  trace = newTrace;
  trace = trace.join('\n')
  trace = trace.replace(RegExp(/ir.assignments\.[A-z]+\.[A-z]+\./g), '');
  return trace;
}

process.stdin.resume();
process.stdin.setEncoding('utf8');
process.stdin
.on('data', chunk => process.stdout.write(doctor(chunk)))
.on('end', () => {
  after.forEach( fn => fn())
})
